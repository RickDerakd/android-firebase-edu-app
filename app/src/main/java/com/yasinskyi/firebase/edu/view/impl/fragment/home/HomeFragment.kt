package com.yasinskyi.firebase.edu.view.impl.fragment.home

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.data.exception.database.UnableLoadUserListException
import com.yasinskyi.firebase.edu.databinding.FragmentHomeBinding
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.main.HomeViewModel
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.util.extension.navigation.navigate
import com.yasinskyi.firebase.edu.util.extension.navigation.navigateSafe
import com.yasinskyi.firebase.edu.util.extension.widget.baseActivity
import com.yasinskyi.firebase.edu.util.extension.widget.isScrolledToEnd
import com.yasinskyi.firebase.edu.view.base.fragment.BaseFragment
import com.yasinskyi.firebase.edu.view.impl.adapter.home.UserAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private lateinit var userAdapter: UserAdapter

    private fun observeUserListState(userList: List<User>?) {
        updateUserList(userList)
    }

    private fun observeSignOutState(isSuccessful: Boolean) {
        if (isSuccessful) {
            navigateToSignInScreen()
        }
    }

    override fun onLoading(isLoading: Boolean) {
        binding?.apply {
            emptyListView.isVisible = userAdapter.currentList.isEmpty() && !isLoading
            retryButton.isClickable = !isLoading
        }
        super.onLoading(isLoading)
    }

    private fun updateUserList(userList: List<User>?) {
        userAdapter.submitList(userList)
        binding?.emptyListView?.isVisible = userAdapter.currentList.isEmpty()
    }

    private fun navigateToSignInScreen() {
        navigateSafe(R.id.actionHomeFragmentToAuthFragment)
    }

    override fun listenViewModel(viewModel: HomeViewModel) {
        super.listenViewModel(viewModel)
        observeWhenCreated(viewModel.signOutState, ::observeSignOutState)
        observeWhenCreated(viewModel.userListState, ::observeUserListState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userAdapter = UserAdapter(navController)
    }

    override fun onBackPressed() {
        showMessageWithPositiveAction(
            R.string.notice_message_title,
            R.string.quit_confirmation_message,
            R.string.yes,
            R.string.no,
            ::closeApp,
        )
    }

    private fun closeApp() {
        baseActivity?.finish()
    }

    override fun onToolbarItemSelected(menuItem: MenuItem) =
        when (menuItem.itemId) {
            R.id.menuSetPhotoButton -> {
                navigate(R.id.actionHomeFragmentToAddPhotoFragment)
                true
            }

            R.id.menuSignOutButton -> {
                viewModel.signOut()
                true
            }

            else -> super.onToolbarItemSelected(menuItem)
        }

    override fun handleError(throwable: Throwable) {
        when (throwable) {
            is UnableLoadUserListException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.unable_to_load_user_list_message,
                    R.string.ok,
                )
            }

            else -> super.handleError(throwable)
        }
    }

    override fun onViewBound(binding: FragmentHomeBinding, savedInstanceState: Bundle?) {
        with(binding) {
            userListView.adapter = userAdapter

            retryButton.setOnClickListener {
                viewModel.loadUsers()
            }
        }
        addScrolledPageListener()

        if (savedInstanceState == null) {
            viewModel.loadUsers()
        }
    }

    private fun addScrolledPageListener() {
        binding?.userListView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (recyclerView.isScrolledToEnd(PAGE_SIZE)) {
                    viewModel.loadUsers()
                }
            }
        })
    }

    companion object {

        const val PAGE_SIZE = 15
    }
}