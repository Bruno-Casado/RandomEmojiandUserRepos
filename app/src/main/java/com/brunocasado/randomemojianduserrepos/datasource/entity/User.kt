package com.brunocasado.randomemojianduserrepos.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey
    val login: String,
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String
) {
    fun isEmpty(): Boolean = this.login.isEmpty()
}

/*
"login": "Bruno-Casado",
  "id": 8345519,
  "node_id": "MDQ6VXNlcjgzNDU1MTk=",
  "avatar_url": "https://avatars.githubusercontent.com/u/8345519?v=4",
  "gravatar_id": "",
  "url": "https://api.github.com/users/Bruno-Casado",
  "html_url": "https://github.com/Bruno-Casado",
  "followers_url": "https://api.github.com/users/Bruno-Casado/followers",
  "following_url": "https://api.github.com/users/Bruno-Casado/following{/other_user}",
  "gists_url": "https://api.github.com/users/Bruno-Casado/gists{/gist_id}",
  "starred_url": "https://api.github.com/users/Bruno-Casado/starred{/owner}{/repo}",
  "subscriptions_url": "https://api.github.com/users/Bruno-Casado/subscriptions",
  "organizations_url": "https://api.github.com/users/Bruno-Casado/orgs",
  "repos_url": "https://api.github.com/users/Bruno-Casado/repos",
  "events_url": "https://api.github.com/users/Bruno-Casado/events{/privacy}",
  "received_events_url": "https://api.github.com/users/Bruno-Casado/received_events",
  "type": "User",
  "site_admin": false,
  "name": null,
  "company": null,
  "blog": "",
  "location": null,
  "email": null,
  "hireable": null,
  "bio": null,
  "twitter_username": null,
  "public_repos": 4,
  "public_gists": 0,
  "followers": 0,
  "following": 0,
  "created_at": "2014-08-03T21:34:16Z",
  "updated_at": "2021-01-27T23:09:25Z"
 */