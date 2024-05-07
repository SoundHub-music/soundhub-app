package com.soundhub.utils.mappers

import com.soundhub.data.model.Post
import com.soundhub.ui.post_editor.PostEditorState
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface PostMapper {
    @Mapping(target = "id", source = "postId")
    fun fromPostEditorStateToPost(state: PostEditorState): Post

    @Mapping(target = "postId", source = "id")
    fun fromPostToPostEditorState(post: Post): PostEditorState

    companion object {
        val impl: PostMapper = Mappers.getMapper(PostMapper::class.java)
    }
}