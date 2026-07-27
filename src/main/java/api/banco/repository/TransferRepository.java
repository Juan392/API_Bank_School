package api.banco.repository;

import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
   @Query("SELECT new api.banco.dto.transfer.TransferHistoryDTO(t.idTransfer, t.amount, t.transferDate, origen.idClient.name, destino.idClient.name) " +
            "FROM Transfer t " +
            "JOIN t.accountOrigin origen " +
            "JOIN t.accountDestiny destino " +
            "WHERE origen.idAccount = :accountId OR destino.idAccount = :accountId " +
            "ORDER BY t.transferDate DESC")
    List<TransferHistoryDTO> searchMovesWithJoin(@Param("accountId") Long id);
}
