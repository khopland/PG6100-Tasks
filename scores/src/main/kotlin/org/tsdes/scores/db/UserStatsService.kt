package org.tsdes.scores.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.transaction.Transactional


@Repository
interface UserStatsRepository : CrudRepository<UserStats, String>


@Service
@Transactional
class UserStatsService(
    val repository: UserStatsRepository,
    val em: EntityManager
) {
    fun registerNewUser(userId: String): Boolean = if (repository.existsById(userId)) false
    else {
        repository.save(UserStats(userId, 0, 0, 0, 0)); true
    }

    fun getNextPage(size: Int, keysetId: String? = null, keysetScore: Int? = null): List<UserStats> = when {
        size < 1 || size > 1000 -> throw IllegalArgumentException("Invalid size value: $size")

        keysetId == null && keysetScore != null || keysetId != null && keysetScore == null ->
            throw IllegalArgumentException("keysetId and keysetScore should be both missing, or both present")

        else -> when (keysetId) {
            null -> em.createQuery(
                "select s from UserStats s order by s.score DESC, s.userId DESC",
                UserStats::class.java
            ).apply { maxResults = size }.resultList
            else -> em.createQuery(
                "select s from UserStats s where s.score<?2 or (s.score=?2 and s.userId<?1) order by s.score DESC, s.userId DESC",
                UserStats::class.java
            ).let { it.setParameter(1, keysetId); it.setParameter(2, keysetScore) }.resultList
        }
    }
}
