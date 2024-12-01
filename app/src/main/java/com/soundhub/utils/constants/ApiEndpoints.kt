package com.soundhub.utils.constants

import com.soundhub.BuildConfig

sealed class ApiEndpoints {
	data object Users {
		const val USER_REQUEST_BODY_NAME = "userDto"

		private const val USERS_ENDPOINT: String = "users"
		const val USER_ID_DYNAMIC_PARAM: String = "userId"
		const val USER_EMAIL_DYNAMIC_PARAM: String = "email"
		const val FRIEND_ID_DYNAMIC_PARAM: String = "friendId"

		const val CURRENT_USER: String = "$USERS_ENDPOINT/currentUser"

		const val CHECK_USER_EXISTENCE = "$USERS_ENDPOINT/checkUser/{$USER_EMAIL_DYNAMIC_PARAM}"

		const val GET_USER_BY_ID: String = "$USERS_ENDPOINT/{$USER_ID_DYNAMIC_PARAM}"
		const val UPDATE_USER: String = "$USERS_ENDPOINT/update/{$USER_ID_DYNAMIC_PARAM}"
		const val ADD_FRIEND: String = "$USERS_ENDPOINT/addFriend/{$FRIEND_ID_DYNAMIC_PARAM}"
		const val DELETE_FRIEND: String = "$USERS_ENDPOINT/deleteFriend/{$FRIEND_ID_DYNAMIC_PARAM}"
		const val GET_RECOMMENDED_FRIENDS = "$USERS_ENDPOINT/recommendedFriends"
		const val GET_FRIENDS = "$USERS_ENDPOINT/{$USER_ID_DYNAMIC_PARAM}/friends"
		const val SEARCH_USER = "$USERS_ENDPOINT/search"
		const val TOGGLE_ONLINE = "$USERS_ENDPOINT/toggleOnline"
		const val COMPATIBLE_USERS = "$USERS_ENDPOINT/compatibleUsers"

		const val SEARCH_PARAM_NAME = "name"
	}

	data object Message {
		private const val MESSAGES_ENDPOINT = "messages"
		const val CHAT_ID_DYNAMIC_PARAM = "chatId"
		const val MESSAGE_ID_DYNAMIC_PARAM = "messageId"

		const val PAGE_PARAM = "page"
		const val PAGE_SIZE_PARAM = "size"
		const val SORT_BY_PARAM = "sort"
		const val ORDER_BY_PARAM = "order"

		const val GET_MESSAGE_BY_ID = "$MESSAGES_ENDPOINT/{$MESSAGE_ID_DYNAMIC_PARAM}"
		const val GET_PAGED_MESSAGES = "$MESSAGES_ENDPOINT/chat/{$CHAT_ID_DYNAMIC_PARAM}"
		const val GET_ALL_UNREAD_MESSAGES = "$MESSAGES_ENDPOINT/unread"
	}

	data object Authentication {
		const val USER_DATA_REQUEST_BODY_NAME = "userData"

		private const val AUTH_ENDPOINT: String = "auth"
		const val SIGN_UP: String = "$AUTH_ENDPOINT/sign-up"
		const val SIGN_IN: String = "$AUTH_ENDPOINT/sign-in"
		const val LOGOUT: String = "$AUTH_ENDPOINT/logout"
		const val REFRESH_TOKEN: String = "$AUTH_ENDPOINT/refresh"
	}

	data object Countries {
		const val ALL_COUNTRIES: String = "all"
	}

	data object Music {
		const val RELEASE_ID_DYNAMIC_PARAM = "releaseId"
		const val ARTIST_ID_DYNAMIC_PARAM = "artistId"
		private const val ARTIST_ENDPOINT = "artists"
		private const val RELEASE_ENDPOINT = "releases"

		const val DATABASE_SEARCH = "database/search"

		const val GET_GENRES = "genres"
		const val GET_ARTISTS = "$ARTIST_ENDPOINT/{$ARTIST_ID_DYNAMIC_PARAM}"
		const val GET_RELEASE_BY_ID = "$RELEASE_ENDPOINT/{$RELEASE_ID_DYNAMIC_PARAM}"
		const val GET_ARTIST_RELEASES = "$GET_ARTISTS/releases"
	}

	data object LastFm {
		private const val API_VERSION = "2.0"
		private const val JSON_RESPONSE_FORMAT = "format=json"
		private const val LAST_FM_API_KEY = "api_key=${BuildConfig.LAST_FM_API_KEY}"

		private const val GET_TOP_ARTISTS_METHOD = "method=tag.gettopartists"
		private const val SEARCH_ARTISTS_METHOD = "method=artist.search"
		private const val GET_MOBILE_SESSION_METHOD = "method=auth.getMobileSession"
		private const val GET_USER_INFO_METHOD = "method=user.getinfo"

		const val USERNAME_PARAM = "username"
		const val PASSWORD_PARAM = "password"
		const val API_SIG_PARAM = "api_sig"
		const val ARTIST_PARAM = "artist"
		const val TAG_PARAM = "tag"
		const val PAGE_PARAM = "page"
		const val USER_PARAM = "user"

		const val GET_USER_INFO =
			"$API_VERSION/?$GET_USER_INFO_METHOD&$LAST_FM_API_KEY&$JSON_RESPONSE_FORMAT"

		const val GET_TOP_ARTISTS_BY_TAG_ENDPOINT = "$API_VERSION/?$GET_TOP_ARTISTS_METHOD&" +
				"$LAST_FM_API_KEY&" +
				JSON_RESPONSE_FORMAT

		const val SEARCH_ARTISTS_ENDPOINT =
			"$API_VERSION/?$SEARCH_ARTISTS_METHOD&$LAST_FM_API_KEY&" +
					JSON_RESPONSE_FORMAT

		const val GET_MOBILE_SESSION = "$API_VERSION/?$GET_MOBILE_SESSION_METHOD&" +
				"$LAST_FM_API_KEY&$JSON_RESPONSE_FORMAT"
	}

	data object Posts {
		const val POST_REQUEST_BODY_NAME = "postDto"
		const val IMAGES_TO_DELETE_NAME = "deleteFiles"

		private const val POSTS_ENDPOINT = "posts"
		const val POST_ID_DYNAMIC_PARAM = "postId"
		const val AUTHOR_ID_DYNAMIC_PARAM = "authorId"

		const val GET_POST_BY_ID = "$POSTS_ENDPOINT/{$POST_ID_DYNAMIC_PARAM}"
		const val GET_POSTS_BY_AUTHOR_ID = "$POSTS_ENDPOINT/post/{$AUTHOR_ID_DYNAMIC_PARAM}"
		const val ADD_POST = "$POSTS_ENDPOINT/add"
		const val TOGGLE_LIKE = "$POSTS_ENDPOINT/like/{$POST_ID_DYNAMIC_PARAM}"
		const val DELETE_POST = "$POSTS_ENDPOINT/delete/{$POST_ID_DYNAMIC_PARAM}"
		const val UPDATE_POST = "$POSTS_ENDPOINT/update/{$POST_ID_DYNAMIC_PARAM}"
	}

	data object Chats {
		private const val CHATS_ENDPOINT = "chats"
		const val CHAT_ID_DYNAMIC_PARAM = "chatId"
		const val USER_ID_DYNAMIC_PARAM = "user_id"
		const val CHAT_NAME_DYNAMIC_PARAM = "newName"

		const val GET_CHATS_BY_CURRENT_USER = "$CHATS_ENDPOINT/user/{$USER_ID_DYNAMIC_PARAM}"
		const val GET_CHAT_BY_ID = "$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}"
		const val DELETE_CHAT = "$CHATS_ENDPOINT/delete/{$CHAT_ID_DYNAMIC_PARAM}"

		const val CREATE_CHAT = "$CHATS_ENDPOINT/single"
		const val CREATE_GROUP_CHAT = "$CHATS_ENDPOINT/group"

		const val ADD_USER_TO_GROUP =
			"$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}/add/{$USER_ID_DYNAMIC_PARAM}"
		const val DELETE_USER_FROM_GROUP =
			"$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}/remove/{$USER_ID_DYNAMIC_PARAM}"

		const val RENAME_GROUP_CHAT =
			"$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}/rename/{$CHAT_NAME_DYNAMIC_PARAM}"
	}

	data object Invites {
		const val INVITES_ENDPOINT = "invites"
		const val RECIPIENT_ID_DYNAMIC_PARAM = "recipientId"
		const val INVITE_ID_DYNAMIC_PARAM = "inviteId"
		const val SENDER_ID_DYNAMIC_PARAM = "senderId"

		const val CREATE_INVITE = "$INVITES_ENDPOINT/create/{$RECIPIENT_ID_DYNAMIC_PARAM}"
		const val ACCEPT_INVITE = "$INVITES_ENDPOINT/accept/{$INVITE_ID_DYNAMIC_PARAM}"
		const val REJECT_INVITE = "$INVITES_ENDPOINT/reject/{$INVITE_ID_DYNAMIC_PARAM}"
		const val DELETE_INVITE = "$INVITES_ENDPOINT/{$INVITE_ID_DYNAMIC_PARAM}"
		const val GET_INVITE_BY_SENDER_AND_RECIPIENT = "$INVITES_ENDPOINT/" +
				"{$SENDER_ID_DYNAMIC_PARAM}/{$RECIPIENT_ID_DYNAMIC_PARAM}"
	}

	data object ChatWebSocket {
		private const val MESSAGE_DYNAMIC_ID = "messageId"

		// topics
		private const val ROOT_MESSAGE_TOPIC = "/queue/messages"
		const val WS_GET_MESSAGES_TOPIC = ROOT_MESSAGE_TOPIC
		const val WS_READ_MESSAGE_TOPIC = "$ROOT_MESSAGE_TOPIC/read"
		const val WS_DELETE_MESSAGE_TOPIC = "$ROOT_MESSAGE_TOPIC/delete"

		// endpoints
		private const val WS_ENDPOINT = "/app"
		const val WS_SEND_MESSAGE_ENDPOINT = "$WS_ENDPOINT/chat"
		const val WS_READ_MESSAGE_ENDPOINT = "$WS_ENDPOINT/message/read/{$MESSAGE_DYNAMIC_ID}"
		const val WS_DELETE_MESSAGE_ENDPOINT = "$WS_ENDPOINT/message/delete/{$MESSAGE_DYNAMIC_ID}"
	}
}