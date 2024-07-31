package com.example.AmbulanceServiceProvider.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ambulance")
@Schema(description = "Details about the ambulance")
public class ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto Generated ID", example = "1")
    private Long id;

    @NotNull
    @Column(name = "number_plate", nullable = false)
    @Schema(description = "Number Plate of the ambulance", example = "AB123CD")
    private String numberplate;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Availability of the ambulance", example = "AVAILABLE")
    private availability status;

    // Constructors
    public ambulance() {}

    public ambulance(String numberplate, availability status) {
        this.numberplate = numberplate;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public availability getStatus() {
        return status;
    }

    public void setStatus(availability status) {
        this.status = status;
    }

    // toString
    @Override
    public String toString() {
        return "Ambulance{" +
                "id=" + id +
                ", numberplate='" + numberplate + '\'' +
                ", status=" + status +
                '}';
    }
}