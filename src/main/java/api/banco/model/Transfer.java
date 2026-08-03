package api.banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transactions")
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transfer")
    private Long idTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account_origin", nullable = false)
    private Account accountOrigin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account_destiny", nullable = false)
    private Account accountDestiny;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date")
    private LocalDateTime transferDate = LocalDateTime.now();

    public Transfer(Account accountOrigin, Account accountDestiny, BigDecimal amount, LocalDateTime transferDate) {
        this.accountOrigin = accountOrigin;
        this.accountDestiny = accountDestiny;
        this.amount = amount;
        this.transferDate = transferDate;
    }
}
