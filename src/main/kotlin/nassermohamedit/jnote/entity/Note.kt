package nassermohamedit.jnote.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "notes")
@SequenceGenerator(name = "notes_id_seq", sequenceName = "notes_id_seq", allocationSize = 1)
class Note {
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notes_id_seq")
    @get:Id
    var id: Long? = null;

    @Column(nullable = false)
    var content: String? = null;

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "unit_id", nullable = false)
    var unit: Unit? = null;

    @get:Column(name = "creation_time", nullable = false)
    var creationTime: LocalDateTime? = null
}