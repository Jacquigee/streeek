package com.bizilabs.streeek.lib.data.mappers

import com.bizilabs.streeek.lib.domain.helpers.DateFormats
import com.bizilabs.streeek.lib.domain.helpers.asDate
import com.bizilabs.streeek.lib.domain.helpers.asString
import com.bizilabs.streeek.lib.domain.models.ContributionDomain
import com.bizilabs.streeek.lib.local.models.ContributionCache
import com.bizilabs.streeek.lib.remote.helpers.asJson
import com.bizilabs.streeek.lib.remote.models.ContributionDTO
import com.bizilabs.streeek.lib.remote.models.EventPayloadDTO
import com.bizilabs.streeek.lib.remote.models.EventPayloadSerializer
import com.bizilabs.streeek.lib.remote.models.GithubActorDTO
import com.bizilabs.streeek.lib.remote.models.GithubEventRepositoryDTO
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

val JsonSerializer = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
    explicitNulls = false
    classDiscriminator = "#class"
}

private fun String.asGithubRepo(): GithubEventRepositoryDTO = JsonSerializer.decodeFromString(this)
private fun String.asActor(): GithubActorDTO = JsonSerializer.decodeFromString(this)
private fun String.asEventPayload(): EventPayloadDTO =
    JsonSerializer.decodeFromString(EventPayloadSerializer, this)

fun ContributionDTO.toDomain() = ContributionDomain(
    id = id,
    createdAt = createdAt.asDate()?.toLocalDateTime(TimeZone.UTC) ?: Clock.System.now().toLocalDateTime(
    TimeZone.UTC),
    accountId = accountId,
    githubEventId = githubEventId,
    githubEventType = githubEventType,
    githubEventDate = githubEventDate.asDate()?.toLocalDateTime(TimeZone.UTC) ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
    githubEventRepo = githubEventRepo.asGithubRepo().toDomain(),
    githubEventActor = githubEventActor.asActor().toDomain(),
    githubEventPayload = githubEventPayload.asEventPayload().toDomain(),
    points = points
)

fun ContributionDomain.toCache() = ContributionCache(
    id = id,
    createdAt = createdAt.asString(DateFormats.ISO_8601_Z) ?: "",
    accountId = accountId,
    githubEventId = githubEventId,
    githubEventType = githubEventType,
    githubEventDate = githubEventDate.asString(DateFormats.ISO_8601_Z) ?: "",
    githubEventRepo = githubEventRepo.toDTO().asJson(),
    githubEventActor = githubEventActor.toDTO().asJson(),
    githubEventPayload = githubEventPayload.toDTO().asJson(),
    points = points
)

fun ContributionCache.toDomain() = ContributionDomain(
    id = id,
    createdAt = createdAt.asDate()?.toLocalDateTime(TimeZone.UTC) ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
    accountId = accountId,
    githubEventId = githubEventId,
    githubEventType = githubEventType,
    githubEventDate = githubEventDate.asDate()?.toLocalDateTime(TimeZone.UTC) ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
    githubEventRepo = githubEventRepo.asGithubRepo().toDomain(),
    githubEventActor = githubEventActor.asActor().toDomain(),
    githubEventPayload = githubEventPayload.asEventPayload().toDomain(),
    points = points
)
