package com.uade.tpo.marketplace.service;

import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uade.tpo.marketplace.entities.*;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.repository.*;
import utilidades.HotelbedsAuthUtil;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.InputStream;

@Service
public class HotelbedsIntegrationService {

    @Value("${hotelbeds.api.key}")
    private String apiKey;

    @Value("${hotelbeds.api.secret}")
    private String apiSecret;

    @Autowired
    private HotelRepository hotelRepository;
    
    @Autowired
    private GestorRepository gestorRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final String baseUrl = "https://api.test.hotelbeds.com/hotel-content-api/1.0";
    private final RestTemplate restTemplate;    public HotelbedsIntegrationService() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(30000);
        
        this.restTemplate = new RestTemplate(requestFactory);
          // Configure message converters with byte array support
        ByteArrayHttpMessageConverter byteArrayConverter = new ByteArrayHttpMessageConverter();
        byteArrayConverter.setSupportedMediaTypes(Arrays.asList(
            MediaType.APPLICATION_OCTET_STREAM,
            new MediaType("application", "*+json"),
            MediaType.APPLICATION_JSON
        ));
        
        this.restTemplate.setMessageConverters(Collections.singletonList(byteArrayConverter));
    }

    public void fetchAndPersistHotels(int from, int to, Long gestorId, Long categoriaId) {
        try {
            long timestamp = System.currentTimeMillis() / 1000;
            String signature = HotelbedsAuthUtil.generateSignature(apiKey, apiSecret, timestamp);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Api-key", apiKey.trim());
            headers.set("X-Signature", signature);
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip");
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // Debug information
            System.out.println("\nRequest Details:");
            System.out.println("API Key: " + apiKey.trim());
            System.out.println("API Secret: " + apiSecret.trim());
            System.out.println("Timestamp: " + timestamp);
            System.out.println("X-Signature: " + signature);
            
            System.out.println("\nHeaders being sent:");
            headers.forEach((key, value) -> System.out.println(key + ": " + headers.getFirst(key)));

            // Modificamos la URL para obtener una lista de hoteles con paginación
            String url = String.format("%s/hotels?fields=code,name,description,address,city,country,rooms,images&language=ENG&from=%d&to=%d", 
                baseUrl, from, to);
            System.out.println("\nRequest URL: " + url);

            // Use byte[] instead of String to handle GZIP
            ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                byte[].class
            );

            System.out.println("\nResponse Status: " + response.getStatusCode());
            System.out.println("Response Headers: " + response.getHeaders());

            if (response.getBody() != null) {
                byte[] responseBody = response.getBody();
                String decompressedBody = null;
                
                try {
                    // Handle GZIP decompression
                    try (GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(responseBody));
                         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = gzis.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                        }
                        decompressedBody = outputStream.toString(StandardCharsets.UTF_8.name());
                    }
                    
                    System.out.println("\nDecompressed Response Body length: " + decompressedBody.length());
                    
                    // Parse the JSON
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(decompressedBody);
                    
                    // Debug: Print the root structure
                    System.out.println("\nRoot node field names:");
                    rootNode.fieldNames().forEachRemaining(fieldName -> 
                        System.out.println("- " + fieldName + " (Type: " + rootNode.get(fieldName).getNodeType() + ")")
                    );
                    
                    if (rootNode.has("error")) {
                        throw new RuntimeException("API Error: " + rootNode.get("error").asText());
                    }
                    
                    // La respuesta de la lista de hoteles tiene una estructura diferente
                    JsonNode hotels = rootNode.path("hotels");
                    
                    if (hotels != null && hotels.isArray()) {
                        System.out.println("\nFound " + hotels.size() + " hotels in the response");
                        processHotelsData(hotels, gestorId, categoriaId);
                    } else {
                        System.out.println("\nNo hotels array found. Full response structure:");
                        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
                    }
                } catch (Exception e) {
                    System.err.println("Error processing response: " + e.getMessage());
                    if (decompressedBody != null) {
                        System.err.println("First 1000 characters of response: " + 
                            decompressedBody.substring(0, Math.min(1000, decompressedBody.length())));
                    }
                    e.printStackTrace();
                    throw new RuntimeException("Failed to process API response", e);
                }
            } else {
                throw new RuntimeException("Empty response body received from Hotelbeds API");
            }
        } catch (Exception e) {
            System.err.println("Error fetching hotels: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch hotels from Hotelbeds API", e);
        }
    }

    private void processHotelsData(JsonNode hotels, Long gestorId, Long categoriaId) {
        int totalHotels = 0;
        int successfullyProcessed = 0;

        for (JsonNode hotel : hotels) {
            try {
                String nombre = hotel.path("name").path("content").asText("");
                if (nombre.isEmpty()) {
                    System.out.println("Skipping hotel with missing name");
                    continue;
                }

                String ciudad = hotel.path("city").path("content").asText("");
                String pais = hotel.path("country").path("description").path("content").asText("");
                String direccion = hotel.path("address").path("content").asText("");
                
                // Verificación estricta de duplicados
                if (hotelRepository.existsByNombreAndDireccion(nombre, direccion)) {
                    System.out.println("Skipping duplicate hotel: " + nombre + " at " + direccion);
                    continue;
                }

                String descripcion = hotel.path("description").path("content").asText("");
                if (descripcion.isEmpty()) {
                    descripcion = "Hermoso hotel ubicado en " + ciudad + ", " + pais;
                }
                
                // Generar email único usando el código del hotel si está disponible
                String hotelCode = hotel.path("code").asText("");
                String email = nombre.toLowerCase().replaceAll(" ", "") + 
                             (hotelCode.isEmpty() ? "_" + System.currentTimeMillis() : "_" + hotelCode) + 
                             "@hotel.com";
                
                // Generar teléfono único usando el código del hotel si está disponible
                String telefono;
                if (!hotelCode.isEmpty()) {
                    telefono = hotelCode.replaceAll("[^0-9]", "");
                    if (telefono.length() > 10) telefono = telefono.substring(0, 10);
                    else if (telefono.length() < 10) {
                        telefono = String.format("%-10s", telefono).replace(' ', '0');
                    }
                } else {
                    telefono = String.format("%010d", new Random().nextInt(1000000000));
                }

                Gestor gestor = gestorRepository.findById(gestorId).orElseThrow(() -> 
                    new RuntimeException("Gestor not found with ID: " + gestorId));
                Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(() ->
                    new RuntimeException("Categoria not found with ID: " + categoriaId));

                Hotel hotelEntity = new Hotel(descripcion, direccion, ciudad, pais, gestor, categoria,
                        nombre, telefono, email);

                // Procesar imágenes
                List<String> imagenesDisponibles = new ArrayList<>();
                JsonNode imagesNode = hotel.path("images");
                if (imagesNode.isArray() && imagesNode.size() > 0) {
                    for (JsonNode image : imagesNode) {
                        String path = image.path("path").asText(null);
                        if (path != null && !path.isEmpty()) {
                            imagenesDisponibles.add("http://photos.hotelbeds.com/giata/bigger/" + path);
                        }
                    }
                }
                
                if (imagenesDisponibles.isEmpty()) {
                    imagenesDisponibles.add("https://source.unsplash.com/random/800x600?luxury-hotel-room");
                    imagenesDisponibles.add("https://source.unsplash.com/random/800x600?hotel-bedroom");
                    imagenesDisponibles.add("https://source.unsplash.com/random/800x600?hotel-suite");
                }

                // Procesar habitaciones
                List<Habitacion> habitaciones = new ArrayList<>();
                JsonNode roomsNode = hotel.path("rooms");
                if (roomsNode.isArray()) {
                    int roomIndex = 0;
                    for (JsonNode roomJson : roomsNode) {
                        if (!roomJson.isMissingNode()) {
                            String roomDescription = roomJson.path("type").path("description").path("content").asText("");
                            TipoHabitacion tipo = mapTipoHabitacion(roomDescription);
                            
                            // Asegurar que el tipo no sea null
                            if (tipo == null) {
                                System.out.println("Warning: Invalid room type for description: " + roomDescription);
                                tipo = TipoHabitacion.DOBLE; // tipo por defecto
                            }
                            
                            int capacidad = roomJson.path("maxAdults").asInt(2);
                            double precio = 50 + new Random().nextInt(151); // entre 50 y 200
                            
                            // Generar un número de habitación significativo basado en el tipo y número secuencial
                            String prefix = switch (tipo) {
                                case SUITE_PREMIUM -> "SP";
                                case SUITE -> "ST";
                                case FAMILIAR -> "FM";
                                case DOBLE -> "DB";
                                case TRIPLE -> "TR";
                                case INDIVIDUAL -> "SG";
                                default -> "RM";
                            };
                            String numero = String.format("%s%03d", prefix, roomIndex + 1);
                            
                            // Seleccionar imagen para la habitación
                            String imagenHabitacion = imagenesDisponibles.get(
                                roomIndex % imagenesDisponibles.size()
                            );
                            
                            Habitacion habitacion = new Habitacion(
                                tipo,
                                capacidad,
                                precio,
                                numero,
                                imagenHabitacion,
                                gestor,
                                categoria
                            );
                            habitacion.setHotel(hotelEntity);
                            habitaciones.add(habitacion);
                            roomIndex++;
                        }
                    }
                }

                if (habitaciones.isEmpty()) {
                    System.out.println("Warning: No valid rooms found for hotel: " + nombre);
                    continue;
                }

                hotelEntity.setHabitaciones(habitaciones);
                hotelRepository.save(hotelEntity);
                successfullyProcessed++;
                System.out.println("Successfully saved hotel: " + nombre + " with " + habitaciones.size() + 
                                " rooms and " + imagenesDisponibles.size() + " unique images");
                
            } catch (Exception e) {
                System.err.println("Error processing hotel data: " + e.getMessage());
                e.printStackTrace();
            }
            totalHotels++;
        }
        
        System.out.println("\nProcessing complete:");
        System.out.println("Total hotels processed: " + totalHotels);
        System.out.println("Successfully saved: " + successfullyProcessed);
        System.out.println("Failed: " + (totalHotels - successfullyProcessed));
    }

    private TipoHabitacion mapTipoHabitacion(String description) {
        if (description == null || description.isEmpty()) {
            return TipoHabitacion.DOBLE;
        }

        description = description.toLowerCase();
        
        // Mapeo más específico
        if (description.contains("premium") || description.contains("luxury suite")) return TipoHabitacion.SUITE_PREMIUM;
        if (description.contains("suite")) return TipoHabitacion.SUITE;
        if (description.contains("family") || description.contains("familiar")) return TipoHabitacion.FAMILIAR;
        if (description.contains("triple")) return TipoHabitacion.TRIPLE;
        if (description.contains("double") || description.contains("doble")) return TipoHabitacion.DOBLE;
        if (description.contains("single") || description.contains("individual")) return TipoHabitacion.INDIVIDUAL;
        if (description.contains("quad")) return TipoHabitacion.CUADRUPLE;
        if (description.contains("shared") || description.contains("compartida")) return TipoHabitacion.COMPARTIDA;
        
        // Por defecto, asumimos habitación doble
        return TipoHabitacion.DOBLE;
    }
}