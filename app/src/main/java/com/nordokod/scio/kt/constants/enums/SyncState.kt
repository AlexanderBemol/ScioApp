package com.nordokod.scio.kt.constants.enums

enum class SyncState(val code: Int) {
    ONLY_IN_LOCAL(1),
    UPDATED_IN_LOCAL(2),
    DELETED_IN_LOCAL(3),
    SYNCHRONIZED(4)
}