package nassermohamedit.jnote.projections

import io.quarkus.runtime.annotations.RegisterForReflection
import nassermohamedit.jnote.entity.Role

@RegisterForReflection
data class Credentials(val id: Long, val password: String, val role: Role)
