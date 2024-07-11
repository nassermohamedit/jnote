package nassermohamedit.jnote.projections

import nassermohamedit.jnote.entity.Role

data class Credentials(val id: Long, val password: String, val role: Role)
