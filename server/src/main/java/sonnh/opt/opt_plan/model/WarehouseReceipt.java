package sonnh.opt.opt_plan.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import sonnh.opt.opt_plan.constant.enums.ReceiptStatus;
import sonnh.opt.opt_plan.constant.enums.ReceiptType;

@Entity
@Table(name = "warehouse_receipts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReceipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReceiptType type; // INBOUND, OUTBOUND

	@Column(nullable = false)
	private LocalDateTime receiptDate;

	@ManyToOne
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "confirmed_by", nullable = true)
	private User confirmedBy;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReceiptStatus status; // DRAFT, PENDING, APPROVED, COMPLETED,
									// CANCELLED

	@OneToMany(mappedBy = "warehouseReceipt", cascade = CascadeType.ALL)
	private List<ReceiptDetail> receiptDetails;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(nullable = true)
	private LocalDateTime confirmedAt;

	@Column(nullable = true)
	private String notes;

	public static String generateCode() {
		return "RC" + LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}
}