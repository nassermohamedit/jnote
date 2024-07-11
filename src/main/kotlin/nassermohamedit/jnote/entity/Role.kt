package nassermohamedit.jnote.entity

import jakarta.persistence.*


@Entity
@Table(name = "roles")
@SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    var id: Short? = null;

    @Column(nullable = false)
    var name: String? = null;
}