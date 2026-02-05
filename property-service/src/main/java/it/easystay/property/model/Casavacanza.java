package it.easystay.property.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "casevacanza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Casavacanza {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String indirizzo;
    
    @Column(nullable = false)
    private Double prezzoNotte;
    
    @Column(nullable = false)
    private String citta;
    
    @Version
    private Long version;
}
