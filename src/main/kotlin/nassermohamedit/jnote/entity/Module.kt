package nassermohamedit.jnote.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "modules")
@SequenceGenerator(name = "modules_id_seq", sequenceName = "modules_id_seq", allocationSize = 1)
class Module {
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modules_id_seq")
    @get:Id
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    var description: String? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "owner_id", nullable = false)
    var owner: User? = null

    @get:Column(name = "creation_time", nullable = false)
    var creationTime: LocalDateTime? = null;

    @get:Column(name = "last_updated", nullable = false)
    var lastUpdated: LocalDateTime? = null
}