package cl.isosalud.service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "products_usage")
@Entity
public class ProductsUsageEntity {
    @Id
    @Column(name = "product_usage_id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointmentEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}