package org.example.sumatyw_backend.cities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cityId;
    private String name;
    @Column(unique = true)
    private String country;
}
