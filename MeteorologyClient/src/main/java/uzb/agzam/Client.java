package uzb.agzam;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        // Добавляем новый сенсор в базу на сервере
        String sensorName = "sensor4";
        addNewSensor(sensorName);

        // Добавляем рандомные значения для этого сенсора
        Random random = new Random();

        double minTemperature = 0.0;
        double maxTemperature = 45.0;
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            addNewMeasurement(random.nextDouble() * maxTemperature,
                    random.nextBoolean(), sensorName);
        }

        // Получаем добавленные значения
        getMeasurements();
    }

    private static void addNewSensor(String newSensorName){
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> jsonToSend = new HashMap<String, String>();
        jsonToSend.put("name", newSensorName);

        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(jsonToSend);
        String url = "http://localhost:8080/sensor/registration";

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Сенсор успешно добавлен!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }

    private static void addNewMeasurement(double value, boolean raining, String sensorName){
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> jsonToSend = new HashMap<>();
        jsonToSend.put("value", value);
        jsonToSend.put("raining", raining);
        jsonToSend.put("sensor", Map.of("name", sensorName));

        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(jsonToSend);
        String url = "http://localhost:8080/measurement/add";

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Измерения успешно добавлены!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }

    private static void getMeasurements(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/measurement";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }
}
