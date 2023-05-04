package uzb.agzam.MeteorologyRestApi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uzb.agzam.MeteorologyRestApi.dto.SensorDTO;
import uzb.agzam.MeteorologyRestApi.models.Sensor;
import uzb.agzam.MeteorologyRestApi.services.SensorService;
import uzb.agzam.MeteorologyRestApi.utl.SensorErrorException;
import uzb.agzam.MeteorologyRestApi.utl.SensorErrorResponse;
import uzb.agzam.MeteorologyRestApi.utl.SensorValidator;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {
    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;
    @Autowired
    public SensorController(SensorService sensorService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public String registration(@RequestBody @Valid SensorDTO sensorDTO,
                               BindingResult bindingResult){
        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new SensorErrorException(errorMsg.toString());
        }

        sensorService.register(sensor);

        return "Новый сенсор зарегистрирован!";
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorErrorException e){
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
