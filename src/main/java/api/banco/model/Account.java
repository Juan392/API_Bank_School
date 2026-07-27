package api.banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAccount;
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client idClient;
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;
    private BigDecimal balance;
    private boolean isActive;
}
