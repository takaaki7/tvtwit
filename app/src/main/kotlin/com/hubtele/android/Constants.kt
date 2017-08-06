package com.hubtele.android

object Constants {
    object PrefKey {
        val PROGRAM_TABLE_REFRESHED_AT = "table_refreshed_at"
        val RANKING_REFRESHED_AT = "ranking_refreshed_at"
        val CHAT_LAUNCHED = "chat_launched"
        val TIMESHIFT_LAUNCHED = "timeshift_launched"
        val COUNT_SEE_ACHIEVE = "count_see_achieve"
        val REVIEWED_VERSION = "reviewed_version"
        val SHALL_NEVER_REVIEW = "never_review"
    }

    object Chat {
        val BOARD_ACTIVITY_REQUEST_CODE = 100
        val ON_ENTRY = "entry_uploaded"
        val ON_ENTRY_LOG = "entry_log"
        val ON_ERROR = "chat_error"
        val ON_PROGRAM_END = "endProgram";
        val RECONNECTED = "reconnected"
        val POST_UPLOAD = "post_upload"
        val INIT = "init"
    }

    object IntentExtraKey {
        val WEB_URL = "web_url"
        val WEB_GA_PAGE ="web_ga_page"
        val TITLE = "toolbar_title"
        val PROGRAM = "program"
        val SEC_USER_SPENT="time_user_spent"
        val LONG_CHAT_CREATED="chat_created_at"
    }

    object URL {
        val MOB_APP = "?mob_app=true";
        val ABOUT = BuildConfig.API_URL + "about" + MOB_APP;
        val INQUIRY = BuildConfig.API_URL + "inquiry" + MOB_APP;
    }
}