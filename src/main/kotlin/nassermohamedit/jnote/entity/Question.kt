package nassermohamedit.jnote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.eclipse.microprofile.openapi.annotations.media.Schema.False


@Entity
@Table(name = "questions")
@SequenceGenerator(name = "questions_id_seq", sequenceName = "questions_id_seq", allocationSize = 1)
class Question {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_id_seq")
    var id: Long? = null

    @Column(nullable = false)
    var question: String? = null;

    @Column(name = "answered", nullable = false)
    var isAnswered: Boolean = false

    var answer: String? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "unit_id", nullable = false)
    var unit: Unit? = null
}