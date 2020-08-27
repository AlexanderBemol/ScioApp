package com.nordokod.scio.kt.model.source.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.nordokod.scio.BuildConfig
import com.nordokod.scio.kt.constants.DataTags
import com.nordokod.scio.kt.constants.GuideException
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.constants.enums.SyncState
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class RemoteGuide(
        private val firebaseFirestore: FirebaseFirestore,
        private val firebaseAuth: FirebaseAuth,
        private val firebaseDynamicLinks: FirebaseDynamicLinks
) {
    /**
     * create a guide in the user collection
     * @param guide: Guide object with the guide
     * @return TaskResult<Guide>
     */
    suspend fun createGuide(guide: Guide): TaskResult<Guide> {
        val uid = firebaseAuth.uid
        return if (uid != null) {
            val data = hashMapOf(
                    Guide::topic.name to guide.topic,
                    Guide::category.name to guide.category,
                    Guide::testDate.name to guide.testDate,
                    Guide::creationDate.name to FieldValue.serverTimestamp(),
                    Guide::updateDate.name to FieldValue.serverTimestamp(),
                    Guide::creationUser.name to uid,
                    Guide::updateUser.name to uid
            )
            try {
                val result = firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .add(data)
                        .await()
                guide.remoteId = result.id
                TaskResult.Success(guide)
            } catch (e: Exception) {
                TaskResult.Error(e)
            }
        } else {
            TaskResult.Error(UnknownException())
        }
    }

    /**
     * update a guide from the user collection
     * @param guide: Guide
     * @return TaskResult<Unit> with the result
     */
    suspend fun updateGuide(guide: Guide): TaskResult<Unit> {
        val uid = firebaseAuth.uid
        return if (uid != null) {
            val data = hashMapOf(
                    Guide::topic.name to guide.topic,
                    Guide::category.name to guide.category,
                    Guide::testDate.name to guide.testDate,
                    Guide::updateUser.name to uid,
                    Guide::updateDate.name to FieldValue.serverTimestamp()
            )
            try {
                firebaseFirestore.collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.remoteId)
                        .update(data)
                        .await()
                TaskResult.Success(Unit)
            } catch (e: Exception) {
                TaskResult.Error(e)
            }
        } else TaskResult.Error(UnknownException())
    }

    /**
     * delete a guide from the user collection
     * @param id: String id of the guide
     * @return TaskResult<Unit>
     */
    suspend fun deleteGuide(id: String): TaskResult<Unit> {
        val uid = firebaseAuth.uid
        return if (uid != null) {
            try {
                firebaseFirestore.collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(id)
                        .delete()
                        .await()
                TaskResult.Success(Unit)
            } catch (e: Exception) {
                TaskResult.Error(e)
            }
        } else TaskResult.Error(UnknownException())
    }

    /**
     * get all the guides of the user
     * @return TaskResult<ArrayList<Guide>>
     */
    suspend fun getUserGuides(): TaskResult<ArrayList<Guide>> {
        val uid = firebaseAuth.uid
        return if (uid != null) {
            try {
                val result = firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .get()
                        .await()
                val guides = ArrayList<Guide>()
                result.forEach {
                    guides.add(
                            Guide(
                                    remoteId = it[Guide::id.name].toString(),
                                    topic = it[Guide::topic.name].toString(),
                                    category = (it[Guide::category.name] as Long).toInt(),
                                    testDate = it.getDate(Guide::testDate.name) ?: Date(),
                                    creationDate = it.getDate(Guide::creationDate.name) ?: Date(),
                                    updateDate = it.getDate(Guide::updateDate.name) ?: Date(),
                                    creationUser = it[Guide::creationUser.name].toString(),
                                    updateUser = it[Guide::updateUser.name].toString()
                            )
                    )
                }
                TaskResult.Success(guides)
            } catch (e: Exception) {
                TaskResult.Error(e)
            }
        } else TaskResult.Error(UnknownException())
    }

    /**
     * generate a link to share the guide
     * @param guide: Guide to share
     * @return TaskResult<String>
     */
    suspend fun generateGuideLink(guide: Guide): TaskResult<String> {
        val uid = firebaseAuth.uid
        return if (uid != null) {
            val link = "https://sendosg.com/?user=" + uid + "&guide=" + guide.id
            val domain = "https://sendosg.com/guides"
            val result = firebaseDynamicLinks.createDynamicLink()
                    .setLink(Uri.parse(link))
                    .setDomainUriPrefix(domain)
                    .setAndroidParameters(
                            DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID).build()
                    )
                    .setSocialMetaTagParameters(
                            DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle("Title")
                                    .setDescription("Description")
                                    .setImageUrl(Uri.parse("https://res.cloudinary.com/teepublic/image/private/s--tWLvXpQb--/t_Preview/b_rgb:d1d1d1,c_limit,f_jpg,h_630,q_90,w_630/v1524281456/production/designs/2612742_0.jpg/"))
                                    .build()
                    )
                    .buildShortDynamicLink()
                    .await()
            TaskResult.Success(result.shortLink.toString())
        } else TaskResult.Error(UnknownException())
    }

    /**
     * get a guide from a DynamicLink
     * @param pendingDynamicLinkData: PendingDynamicLinkData dynamicLink of the guide
     * @return TaskResult<Guide>
     */
    suspend fun getPublicGuide(pendingDynamicLinkData: PendingDynamicLinkData): TaskResult<Guide> {
        return if (pendingDynamicLinkData.link != null) {
            val uid = pendingDynamicLinkData.link.getQueryParameter(DataTags.USER_QUERY)
            val guideId = pendingDynamicLinkData.link.getQueryParameter(DataTags.GUIDE_QUERY)
            if (uid != null && guideId != null) {
                val result = firebaseFirestore.collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guideId)
                        .get()
                        .await()
                TaskResult.Success(
                        Guide(
                                remoteId = result[Guide::id.name].toString(),
                                topic = result[Guide::topic.name].toString(),
                                category = (result[Guide::category.name] as Long).toInt(),
                                testDate = result.getDate(Guide::testDate.name) ?: Date(),
                                creationDate = result.getDate(Guide::creationDate.name) ?: Date(),
                                updateDate = result.getDate(Guide::updateDate.name) ?: Date(),
                                creationUser = result[Guide::creationUser.name].toString(),
                                updateUser = result[Guide::updateUser.name].toString()
                        )
                )
            } else TaskResult.Error(GuideException(GuideException.Code.GUIDE_NOT_AVAILABLE))
        } else TaskResult.Error(UnknownException())
    }

    /**
     * import a guide to the user guides collection
     * @param guide: Guide
     * @return TaskResult<Guide>
     */
    suspend fun importGuide(guide: Guide): TaskResult<Unit> {
        val uid = firebaseAuth.uid
        return if (uid != null) {
            try {
                val guideDocument = firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.creationUser)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.remoteId)
                        .get()
                        .await()

                val data = guideDocument.data
                if (data != null) {
                    data[Guide::updateUser.name] = uid
                    data[Guide::updateDate.name] = FieldValue.serverTimestamp()
                    firebaseFirestore
                            .collection(DataTags.GUIDES_COLLECTION)
                            .document(uid)
                            .collection(DataTags.GUIDES_COLLECTION)
                            .add(data)
                            .await()
                    TaskResult.Success(Unit)
                } else TaskResult.Error(GuideException(GuideException.Code.GUIDE_NOT_AVAILABLE))
            } catch (e: Exception) {
                TaskResult.Error(e)
            }
        } else {
            TaskResult.Error(UnknownException())
        }
    }


}