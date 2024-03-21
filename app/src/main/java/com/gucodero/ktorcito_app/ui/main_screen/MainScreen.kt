package com.gucodero.ktorcito_app.ui.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gucodero.ktorcito_app.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gucodero.ktorcito_app.common.capitalize
import com.gucodero.ktorcito_app.domain.post.entity.Post
import com.gucodero.ktorcito_app.ui.common.LoadingScaffold
import com.gucodero.ktorcito_app.ui.common.OnEvent
import com.gucodero.ktorcito_app.ui.common.isLoading
import com.gucodero.ktorcito_app.ui.common.uiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val isLoading: Boolean by viewModel.isLoading()
    val uiState: MainUiState by viewModel.uiState()
    var selectedUser: Int? by remember {
        mutableStateOf(null)
    }
    var expanded: Boolean by remember {
        mutableStateOf(false)
    }
    var postDetail: Post? by remember {
        mutableStateOf(null)
    }
    var postEdit: Post? by remember {
        mutableStateOf(null)
    }
    var showCreatorPost by remember {
        mutableStateOf(false)
    }
    OnEvent(
        channel = viewModel.uiEventChannel
    ){
        when(it){
            is MainUiEvent.ShowPostDetails -> {
                postDetail = it.post
            }
            is MainUiEvent.ClosePostEdit -> {
                postEdit = null
            }
            is MainUiEvent.ClosePostCreate -> {
                showCreatorPost = false
            }
        }
    }
    if (postDetail != null) {
        PostInfoDialog(
            title = postDetail!!.title,
            body = postDetail!!.body
        ) {
            postDetail = null
        }
    }
    postEdit?.let { post ->
        PostEditDialog(
            title = post.title,
            body = post.body,
            onSuccess = { newBody ->
                viewModel.updatePost(
                    id = post.id,
                    body = newBody,
                    title = post.title,
                    userId = post.userId
                )
            },
            onCancel = {
                postEdit = null
            }
        )
    }
    if (showCreatorPost) {
        CreatePostDialog(
            onSuccess = { title, body ->
                viewModel.createPost(
                    userId = 1,
                    title = title,
                    body = body
                )
            },
            onCancel = {
                showCreatorPost = false
            }
        )
    }
    LoadingScaffold(
        isLoading = isLoading,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showCreatorPost = true
                },
                containerColor = Color(0xFFFFFFFF),
                contentColor = Color(0xFF3086EE),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        },
        topBar = {
            Text(
                text = stringResource(id = R.string.all_posts),
                modifier = Modifier.padding(
                        start = 24.dp,
                        top = 24.dp
                    ),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        all = 24.dp
                    )
            ) {
                TextField(
                    value = if (selectedUser != null) {
                        stringResource(
                            id = R.string.user_x,
                            selectedUser.toString()
                        )
                    } else {
                        stringResource(
                            id = R.string.all,
                        )
                    },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = R.string.all)
                            )
                        },
                        onClick = {
                            selectedUser = null
                            expanded = false
                            viewModel.getPosts()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.users.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = R.string.user_x,
                                        item.toString()
                                    )
                                )
                            },
                            onClick = {
                                selectedUser = item
                                expanded = false
                                viewModel.getPosts(
                                    userId = selectedUser!!
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    ) {
        val (
            bodyRef: ConstrainedLayoutReference
        ) = createRefs()
        LazyColumn(
            modifier = Modifier.constrainAs(bodyRef) {
                height = Dimension.fillToConstraints
                width = Dimension.matchParent
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp
            )
        ) {
            itemsIndexed(
                items = uiState.posts,
                key = { _, post ->
                    post.id
                }
            ) { index, post ->
                PostItem(
                    userId = post.userId,
                    title = post.title,
                    body = post.body,
                    onEdit = {
                        postEdit = post
                    },
                    onDelete = {
                        viewModel.deletePost(
                            id = post.id
                        )
                    },
                    onClick = {
                        viewModel.getPostById(
                            id = post.id
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = if (uiState.posts.lastIndex != index) 24.dp else 0.dp
                        )
                )
            }
        }
    }
}

@Composable
fun PostItem(
    userId: Int,
    title: String,
    body: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val padding: Dp = 24.dp
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(title.capitalize())
                }
                append(" (userId: $userId)")
            },
            modifier = Modifier.padding(
                top = padding,
                start = padding,
                end = padding
            )
        )
        Text(
            text = body.capitalize(),
            modifier = Modifier.padding(
                top = padding,
                start = padding,
                end = padding,
                bottom = padding
            ),
            maxLines = 2
        )
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(
                    bottom = padding,
                    end = padding
                )
        ) {
            FloatingActionButton(
                onClick = onEdit,
                containerColor = Color(0xFF3086EE),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            FloatingActionButton(
                onClick = onDelete,
                containerColor = Color(0xFFE03838),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun PostInfoDialog(
    title: String,
    body: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title.capitalize()
            )
        },
        text = {
            Text(
                text = body.capitalize()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(id = R.string.ok)
                )
            }
        }
    )
}

@Composable
fun PostEditDialog(
    title: String,
    body: String,
    onSuccess: (body: String) -> Unit,
    onCancel: () -> Unit
) {
    var text: String by remember {
        mutableStateOf(body.capitalize())
    }
    AlertDialog(
        onDismissRequest = {
            onCancel()
        },
        title = {
            Text(
                text = title.capitalize()
            )
        },
        text = {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSuccess(text)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.ok)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
        }
    )
}

@Composable
fun CreatePostDialog(
    onSuccess: (title: String, body: String) -> Unit,
    onCancel: () -> Unit
) {
    var title: String by remember {
        mutableStateOf("")
    }
    var body: String by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = {
            onCancel()
        },
        title = {
            Text(
                text = stringResource(id = R.string.create_post)
            )
        },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.title)
                        )
                    }
                )
                TextField(
                    value = body,
                    onValueChange = {
                        body = it
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.body)
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSuccess(title, body)
                },
                enabled = title.isNotBlank() && body.isNotBlank()
            ) {
                Text(
                    text = stringResource(id = R.string.ok)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
        }
    )
}