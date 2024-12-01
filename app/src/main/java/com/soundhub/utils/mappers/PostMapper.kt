package com.soundhub.utils.mappers

import com.soundhub.data.states.PostEditorState
import com.soundhub.domain.model.Post
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface PostMapper {
	@Mapping(target = "id", source = "postId")
	fun fromPostEditorStateToPost(state: PostEditorState): Post

	@Mapping(target = "postId", source = "id")
	@Mapping(target = "newImages", ignore = true)
	@Mapping(target = "imagesToBeDeleted", ignore = true)
	@Mapping(target = "doesPostExist", ignore = true)
	@Mapping(target = "oldPostState", ignore = true)
	fun fromPostToPostEditorState(post: Post): PostEditorState

	companion object {
		val impl: PostMapper = Mappers.getMapper(PostMapper::class.java)
	}
}