package com.soundhub.utils

sealed class ApiEndpoints {
    data object Users {
        private const val USERS_ENDPOINT: String = "users"
        const val USER_ID_DYNAMIC_PARAM: String = "userId"
        const val FRIEND_ID_DYNAMIC_PARAM: String = "friendId"

        const val CURRENT_USER: String = "$USERS_ENDPOINT/currentUser"

        const val GET_USER_BY_ID: String = "$USERS_ENDPOINT/{$USER_ID_DYNAMIC_PARAM}"
        const val UPDATE_USER: String = "$USERS_ENDPOINT/update/{$USER_ID_DYNAMIC_PARAM}"
        const val ADD_FRIEND: String = "$USERS_ENDPOINT/addFriend/{$FRIEND_ID_DYNAMIC_PARAM}"
        const val DELETE_FRIEND: String = "$USERS_ENDPOINT/deleteFriend/{$FRIEND_ID_DYNAMIC_PARAM}"
        const val GET_RECOMMENDED_FRIENDS = "$USERS_ENDPOINT/{$USER_ID_DYNAMIC_PARAM}/recommendedFriends"
        const val GET_FRIENDS = "$USERS_ENDPOINT/{$USER_ID_DYNAMIC_PARAM}/friends"
    }

    data object Authentication {
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
        private const val ARTIST_ENDPOINT: String = "artists"
        private const val RELEASE_ENDPOINT: String = "releases"

        const val DATABASE_SEARCH: String = "database/search"

        const val ARTISTS: String = "$ARTIST_ENDPOINT/{$ARTIST_ID_DYNAMIC_PARAM}"
        const val RELEASES: String = "$RELEASE_ENDPOINT/{$RELEASE_ID_DYNAMIC_PARAM}"
        const val ARTIST_RELEASES = "$ARTISTS/releases"
    }

   data object Files {
        private const val FILES_ENDPOINT: String = "files"
        const val GET_FILE: String = "$FILES_ENDPOINT/{filename}"
    }

    data object Posts {
        private const val POSTS_ENDPOINT = "posts"
        const val POST_ID_DYNAMIC_PARAM = "postId"
        const val AUTHOR_ID_DYNAMIC_PARAM = "authorId"

        const val GET_POST_BY_ID = "$POSTS_ENDPOINT/{$POST_ID_DYNAMIC_PARAM}"
        const val GET_POSTS_BY_AUTHOR_ID = "$POSTS_ENDPOINT/post/{$AUTHOR_ID_DYNAMIC_PARAM}"
        const val ADD_POST = "$POSTS_ENDPOINT/add"
        const val TOGGLE_LIKE = "$POSTS_ENDPOINT/like/{$POST_ID_DYNAMIC_PARAM}"
        const val DELETE = "$POSTS_ENDPOINT/delete/{$POST_ID_DYNAMIC_PARAM}"
        const val UPDATE = "$POSTS_ENDPOINT/update/{$POST_ID_DYNAMIC_PARAM}"
    }

    data object Chats {
        private const val CHATS_ENDPOINT = "chats"
        const val CHAT_ID_DYNAMIC_PARAM = "chatId"
        const val USER_ID_DYNAMIC_PARAM = "user_id"
        const val CHAT_NAME_DYNAMIC_PARAM = "newName"

        const val GET_CHATS_BY_CURRENT_USER = "$CHATS_ENDPOINT/user"
        const val GET_CHAT_BY_ID = "$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}"
        const val DELETE_CHAT = "$CHATS_ENDPOINT/delete/{$CHATS_ENDPOINT}"

        const val CREATE_CHAT = "$CHATS_ENDPOINT/single"
        const val CREATE_GROUP_CHAT = "$CHATS_ENDPOINT/group"

        const val ADD_USER_TO_GROUP = "$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}/add/{$USER_ID_DYNAMIC_PARAM}"
        const val DELETE_USER_FROM_GROUP = "$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}/remove/{$USER_ID_DYNAMIC_PARAM}"

        const val RENAME_GROUP_CHAT = "$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}/rename/{$CHAT_NAME_DYNAMIC_PARAM}"
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
        const val GET_ALL_INVITES_BY_SENDER_ID = "$INVITES_ENDPOINT/{$SENDER_ID_DYNAMIC_PARAM}"
    }

    data object ChatWebSocket {
        private const val MESSAGES_ENDPOINT = "messages"
        const val MESSAGE_DYNAMIC_ID = "messageId"
        const val CHAT_DYNAMIC_ID = "chatId"

        const val SEND_MESSAGE = "$MESSAGES_ENDPOINT/chat"
        const val GET_MESSAGES = "$MESSAGES_ENDPOINT/chat/{$CHAT_DYNAMIC_ID}"
        const val GET_MESSAGE_BY_ID = "$MESSAGES_ENDPOINT/{$MESSAGE_DYNAMIC_ID}"
        const val DELETE_MESSAGE = "$MESSAGES_ENDPOINT/message/delete/{$MESSAGE_DYNAMIC_ID}"

        const val MARK_MESSAGE_AS_READ = "$MESSAGES_ENDPOINT/message/read/{$MESSAGE_DYNAMIC_ID}"
    }
}