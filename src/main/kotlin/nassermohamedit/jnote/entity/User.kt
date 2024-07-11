package nassermohamedit.jnote.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
class User {
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @get:Id
    var id: Long? = null

    @get:Column(nullable = false)
    var name: String? = null

    @get:Column(unique = true, nullable = false)
    var username: String? = null;

    @get:Column(nullable = false)
    var password: String? = null;

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "role_id", nullable = false)
    var role: Role? = null;

    @get:Column(unique = true, nullable = false)
    var email: String? = null;

    @get:Column(name = "creation_time", nullable = false)
    var creationTime: LocalDateTime? = null;
}
