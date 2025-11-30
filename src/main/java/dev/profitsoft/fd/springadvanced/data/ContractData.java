package dev.profitsoft.fd.springadvanced.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

/**
 * Contract persistent entity.
 */
@Getter
@Setter
@Entity
@Table(name = "contract")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContractData {

  /**
   * Contract ID.
   */
  @Id
  @EqualsAndHashCode.Include
  private String id;

  /**
   * Contract number.
   * Unique value.
   */
  private String number;

  /**
   * Contract sign date.
   */
  private LocalDate signDate;

  /**
   * Payments associated with this contract.
   */
  @OneToMany(mappedBy = "contract")
  private Set<PaymentData> payments;

  /**
   * Timestamp when contract was saved.
   */
  private Instant savedAt;

}
