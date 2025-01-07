package com.bizilabs.streeek.lib.remote.sources.issues

import com.bizilabs.streeek.lib.remote.helpers.GithubEndpoint
import com.bizilabs.streeek.lib.remote.helpers.NetworkResult
import com.bizilabs.streeek.lib.remote.helpers.safeApiCall
import com.bizilabs.streeek.lib.remote.models.CommentDTO
import com.bizilabs.streeek.lib.remote.models.CreateIssueDTO
import com.bizilabs.streeek.lib.remote.models.GithubIssueDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

interface IssuesRemoteSource {
    suspend fun createIssue(request: CreateIssueDTO): NetworkResult<GithubIssueDTO>

    suspend fun fetchUserIssues(
        username: String,
        page: Int,
    ): NetworkResult<List<GithubIssueDTO>>

    suspend fun fetchIssues(page: Int): NetworkResult<List<GithubIssueDTO>>

    suspend fun fetchIssue(number: Long): NetworkResult<GithubIssueDTO>

    suspend fun fetchIssueComments(
        number: Long,
        page: Int,
    ): NetworkResult<List<CommentDTO>>
}

class IssuesRemoteSourceImpl(
    private val client: HttpClient,
) : IssuesRemoteSource {
    override suspend fun createIssue(request: CreateIssueDTO): NetworkResult<GithubIssueDTO> =
        safeApiCall {
            client.post {
                url(GithubEndpoint.Repository.Issues.url)
                setBody(body = request)
            }
        }

    override suspend fun fetchUserIssues(
        username: String,
        page: Int,
    ): NetworkResult<List<GithubIssueDTO>> =
        safeApiCall {
            client.get {
                url(GithubEndpoint.Repository.Issues.url)
                parameter("creator", username)
                parameter("page", page)
            }
        }

    override suspend fun fetchIssues(page: Int): NetworkResult<List<GithubIssueDTO>> =
        safeApiCall {
            client.get {
                url(GithubEndpoint.Repository.Issues.url)
                parameter("page", page)
            }
        }

    override suspend fun fetchIssue(number: Long): NetworkResult<GithubIssueDTO> =
        safeApiCall {
            client.get {
                url(GithubEndpoint.Repository.Issues(id = number).url)
            }
        }

    override suspend fun fetchIssueComments(
        number: Long,
        page: Int,
    ): NetworkResult<List<CommentDTO>> =
        safeApiCall {
            client.get {
                url(GithubEndpoint.Repository.Issues(id = number).Comments().url)
                parameter("page", page)
            }
        }
}
