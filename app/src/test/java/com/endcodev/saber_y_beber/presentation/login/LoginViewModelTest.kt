package com.endcodev.saber_y_beber.presentation.login

/*
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var resourcesProvider: ResourcesProvider

    @Mock
    private lateinit var authenticationService: AuthenticationService

    @Mock
    lateinit var firebase: FirebaseClient


    @Before
    fun setup() {
        // Assuming proper initialization of resourcesProvider and authenticationService
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(resourcesProvider, authenticationService, firebase)
    }

    @Test
    fun `test invalid email`() {
        // Arrange
        val invalidEmail = "invalidemail"
        val expectedErrorMessage = "Invalid email"

        // Act
        viewModel.createAccount(invalidEmail, "password", "password", "username")

        // Assert
        assertEquals(expectedErrorMessage, viewModel.email.value)
    }

    @Test
    fun `test invalid password`() {
        // Arrange
        val invalidPassword = "12345"
        val expectedErrorMessage = "Invalid password"

        // Act
        viewModel.createAccount("test@example.com", invalidPassword, invalidPassword, "username")

        // Assert
        assertEquals(expectedErrorMessage, viewModel.pass.value)
    }

    @Test
    fun `test password mismatch`() {
        // Arrange
        val password = "Abcd123!"
        val repeatPassword = "Abcd456!"
        val expectedErrorMessage = "Passwords do not match"

        // Act
        viewModel.createAccount("test@example.com", password, repeatPassword, "username")

        // Assert
        assertEquals(expectedErrorMessage, viewModel.repeat.value)
    }

    @Test
    fun `test invalid username`() {
        // Arrange
        val invalidUsername = "us"
        val expectedErrorMessage = "Invalid username"

        // Act
        viewModel.createAccount("test@example.com", "password", "password", invalidUsername)

        // Assert
        assertEquals(expectedErrorMessage, viewModel.user.value)
    }
}
*/
