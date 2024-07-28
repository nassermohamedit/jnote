package nassermohamedit.jnote.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "units")
@SequenceGenerator(name = "units_id_seq", sequenceName = "units_id_seq", allocationSize = 1)
class Unit {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "units_id_seq")
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    var description: String? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "module_id", nullable = false)
    var module: Module? = null

    @get:Column(name = "creation_time", nullable = false)
    var creationTime: LocalDateTime? = null;

    @get:Column(name = "last_updated", nullable = false)
    var lastUpdated: LocalDateTime? = null

    @Column(nullable = false)
    var self: Boolean? = false;

    @get:OneToMany(cascade = [CascadeType.REMOVE], mappedBy = "unit")
    lateinit var notes: List<Note>

    @get:OneToMany(cascade = [CascadeType.REMOVE], mappedBy = "unit")
    lateinit var questions: List<Question>
}