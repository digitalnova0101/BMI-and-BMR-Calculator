package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

enum class Screen {
  Home,
  Calculator,
  PrivacyPolicy,
  Disclaimer
}

/**
 * MainActivity is the main entry point of the Android application.
 * ComponentActivity is the base class for activities that use Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Configures the app to draw from edge-to-edge (behind system status & navigation bars).
    enableEdgeToEdge()
    
    setContent {
      // Wrapper for our custom emerald-green and white Material 3 theme
      MyApplicationTheme {
        var currentScreen by remember { mutableStateOf(Screen.Home) }
        
        when (currentScreen) {
          Screen.Home -> HomeScreen(
            onStartCalculator = { currentScreen = Screen.Calculator },
            onNavigateToPrivacy = { currentScreen = Screen.PrivacyPolicy },
            onNavigateToDisclaimer = { currentScreen = Screen.Disclaimer }
          )
          Screen.Calculator -> CalculatorScreen(
            onNavigateBack = { currentScreen = Screen.Home }
          )
          Screen.PrivacyPolicy -> PrivacyPolicyScreen(
            onNavigateBack = { currentScreen = Screen.Home }
          )
          Screen.Disclaimer -> DisclaimerScreen(
            onNavigateBack = { currentScreen = Screen.Home }
          )
        }
      }
    }
  }
}

/**
 * HomeScreen is our main screen Composable.
 * It contains the logo, app title, tagline, a clean summary card, the start button, and bottom links.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onStartCalculator: () -> Unit,
  onNavigateToPrivacy: () -> Unit,
  onNavigateToDisclaimer: () -> Unit
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val scrollState = rememberScrollState()

  Scaffold(
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    contentWindowInsets = WindowInsets.safeDrawing,
    containerColor = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 24.dp)
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      // Top spacing
      Spacer(modifier = Modifier.height(16.dp))

      // 1. App Logo (Simple green health icon)
      AppLogoPlaceholder(modifier = Modifier.testTag("app_logo"))

      // 2. Title Section
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = "BMR & BMI Calculator",
          style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("app_title")
        )
        Text(
          text = "Know your body better.",
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
          ),
          textAlign = TextAlign.Center
        )
      }

      // 3. Description text
      Text(
        text = "Calculate your BMI and estimate your daily calorie needs in less than 10 seconds.",
        style = MaterialTheme.typography.bodyLarge.copy(
          lineHeight = 24.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .testTag("welcome_message")
      )

      // 4. Feature Cards
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        // Card 1: BMI
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("info_card"),
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          )
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Text(
              text = "📏",
              style = MaterialTheme.typography.headlineMedium
            )
            Column {
              Text(
                text = "BMI Calculator",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Check whether your weight is healthy.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        // Card 2: BMR
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          )
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Text(
              text = "🔥",
              style = MaterialTheme.typography.headlineMedium
            )
            Column {
              Text(
                text = "BMR Calculator",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Estimate calories your body burns at rest.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 5. Large green button: Start Calculator
      Button(
        onClick = onStartCalculator,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("start_calculator_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 0.dp,
          pressedElevation = 0.dp
        )
      ) {
        Text(
          text = "Start Calculator",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.2.sp
          )
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bottom Links & Info
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Bottom links
        Row(
          modifier = Modifier.testTag("bottom_links"),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
              .clickable { onNavigateToPrivacy() }
              .padding(horizontal = 8.dp, vertical = 6.dp)
              .testTag("privacy_policy_link")
          )
          Text(
            text = "•",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
          )
          Text(
            text = "Disclaimer",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
              .clickable { onNavigateToDisclaimer() }
              .padding(horizontal = 8.dp, vertical = 6.dp)
              .testTag("disclaimer_link")
          )
          Text(
            text = "•",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
          )
          Text(
            text = "Version 1.0",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
              fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }
      }
    }
  }
}

/**
 * A simple, clean, minimalist App Logo placeholder.
 * Features a flat green rounded container with a high-contrast white heart icon.
 */
@Composable
fun AppLogoPlaceholder(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .size(80.dp)
      .background(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(20.dp)
      ),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.Favorite,
      contentDescription = "Health App Logo",
      tint = Color.White,
      modifier = Modifier.size(40.dp)
    )
  }
}

/**
 * Android Studio Preview function to preview our beautifully crafted landing page.
 */
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomePreview() {
  MyApplicationTheme {
    HomeScreen(
      onStartCalculator = {},
      onNavigateToPrivacy = {},
      onNavigateToDisclaimer = {}
    )
  }
}

enum class Gender {
  Male, Female
}

data class ActivityLevel(
  val name: String,
  val multiplier: Double,
  val description: String
)

data class CalculatorResult(
  val bmi: Double,
  val bmiCategory: String,
  val bmr: Double,
  val tdee: Double
)

/**
 * CalculatorScreen is the screen where the user enters health data to compute BMR & BMI.
 * It strictly adheres to Material Design 3 and our green/white Geometric Balance style.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onNavigateBack: () -> Unit) {
  var gender by remember { mutableStateOf(Gender.Male) }
  var ageInput by remember { mutableStateOf("") }
  var heightInput by remember { mutableStateOf("") }
  var weightInput by remember { mutableStateOf("") }
  
  var ageError by remember { mutableStateOf<String?>(null) }
  var heightError by remember { mutableStateOf<String?>(null) }
  var weightError by remember { mutableStateOf<String?>(null) }

  val activityLevels = remember {
    listOf(
      ActivityLevel("Sedentary", 1.2, "Little or no exercise"),
      ActivityLevel("Lightly Active", 1.375, "Light exercise 1-3 days/week"),
      ActivityLevel("Moderately Active", 1.55, "Moderate exercise 3-5 days/week"),
      ActivityLevel("Very Active", 1.725, "Hard exercise 6-7 days/week"),
      ActivityLevel("Super Active", 1.9, "Very hard daily exercise / physical job")
    )
  }
  var dropdownExpanded by remember { mutableStateOf(false) }
  var selectedActivity by remember { mutableStateOf(activityLevels[0]) }

  var result by remember { mutableStateOf<CalculatorResult?>(null) }
  var showBottomSheet by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Calculator",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to home"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
          navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
      )
    },
    contentWindowInsets = WindowInsets.safeDrawing,
    containerColor = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 24.dp)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Enter your details below to calculate your body parameters.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      // Gender Selection Section
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "Gender",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          GenderSelectionCard(
            selected = gender == Gender.Male,
            label = "Male",
            onClick = { gender = Gender.Male },
            modifier = Modifier.weight(1f).testTag("gender_male")
          )
          GenderSelectionCard(
            selected = gender == Gender.Female,
            label = "Female",
            onClick = { gender = Gender.Female },
            modifier = Modifier.weight(1f).testTag("gender_female")
          )
        }
      }

      // Age Input
      OutlinedTextField(
        value = ageInput,
        onValueChange = {
          ageInput = it
          ageError = null
        },
        label = { Text("Age (years)") },
        placeholder = { Text("e.g. 25") },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Next
        ),
        isError = ageError != null,
        supportingText = {
          if (ageError != null) {
            Text(
              text = ageError!!,
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall
            )
          }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("age_input")
      )

      // Height Input
      OutlinedTextField(
        value = heightInput,
        onValueChange = {
          heightInput = it
          heightError = null
        },
        label = { Text("Height (cm)") },
        placeholder = { Text("e.g. 175") },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Next
        ),
        isError = heightError != null,
        supportingText = {
          if (heightError != null) {
            Text(
              text = heightError!!,
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall
            )
          }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("height_input")
      )

      // Weight Input
      OutlinedTextField(
        value = weightInput,
        onValueChange = {
          weightInput = it
          weightError = null
        },
        label = { Text("Weight (kg)") },
        placeholder = { Text("e.g. 70") },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Done
        ),
        isError = weightError != null,
        supportingText = {
          if (weightError != null) {
            Text(
              text = weightError!!,
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall
            )
          }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("weight_input")
      )

      // Activity level dropdown
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "Activity Level",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
        ExposedDropdownMenuBox(
          expanded = dropdownExpanded,
          onExpandedChange = { dropdownExpanded = it }
        ) {
          OutlinedTextField(
            value = "${selectedActivity.name} (${selectedActivity.multiplier})",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expand activity levels"
              )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
              .testTag("activity_dropdown"),
            colors = OutlinedTextFieldDefaults.colors()
          )
          ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
          ) {
            activityLevels.forEach { level ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(
                      text = "${level.name} (${level.multiplier})",
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                      text = level.description,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                },
                onClick = {
                  selectedActivity = level
                  dropdownExpanded = false
                },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Calculate Button
      Button(
        onClick = {
          ageError = null
          heightError = null
          weightError = null

          val ageValue = ageInput.trim().toIntOrNull()
          val heightValue = heightInput.trim().toDoubleOrNull()
          val weightValue = weightInput.trim().toDoubleOrNull()

          var isValid = true

          // Age Validation
          if (ageInput.trim().isEmpty()) {
            ageError = "Please enter your age"
            isValid = false
          } else if (ageValue == null) {
            ageError = "Please enter a valid whole number"
            isValid = false
          } else if (ageValue <= 0 || ageValue > 120) {
            ageError = "Please enter a realistic age (1-120)"
            isValid = false
          }

          // Height Validation
          if (heightInput.trim().isEmpty()) {
            heightError = "Please enter your height"
            isValid = false
          } else if (heightValue == null) {
            heightError = "Please enter a valid height"
            isValid = false
          } else if (heightValue < 50.0 || heightValue > 280.0) {
            heightError = "Please enter a realistic height in cm (50-280)"
            isValid = false
          }

          // Weight Validation
          if (weightInput.trim().isEmpty()) {
            weightError = "Please enter your weight"
            isValid = false
          } else if (weightValue == null) {
            weightError = "Please enter a valid weight"
            isValid = false
          } else if (weightValue < 10.0 || weightValue > 600.0) {
            weightError = "Please enter a realistic weight in kg (10-600)"
            isValid = false
          }

          if (isValid && ageValue != null && heightValue != null && weightValue != null) {
            // BMI Calculation
            val heightInMeters = heightValue / 100.0
            val bmi = weightValue / (heightInMeters * heightInMeters)

            val bmiCategory = when {
              bmi < 18.5 -> "Underweight"
              bmi < 25.0 -> "Normal weight"
              bmi < 30.0 -> "Overweight"
              else -> "Obese"
            }

            // BMR Calculation (Mifflin-St Jeor Equation)
            val bmr = if (gender == Gender.Male) {
              (10.0 * weightValue) + (6.25 * heightValue) - (5.0 * ageValue) + 5.0
            } else {
              (10.0 * weightValue) + (6.25 * heightValue) - (5.0 * ageValue) - 161.0
            }

            // TDEE Calculation
            val tdee = bmr * selectedActivity.multiplier

            result = CalculatorResult(
              bmi = bmi,
              bmiCategory = bmiCategory,
              bmr = bmr,
              tdee = tdee
            )
            showBottomSheet = true
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .testTag("calculate_button"),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        )
      ) {
        Text(
          text = "Calculate",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }

      Spacer(modifier = Modifier.height(40.dp))
    }

    if (showBottomSheet && result != null) {
      ModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        containerColor = Color.White,
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.40f),
        dragHandle = {
          BottomSheetDefaults.DragHandle(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
          )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
      ) {
        ResultsBottomSheetContent(
          result = result!!,
          onDismiss = { showBottomSheet = false }
        )
      }
    }
  }
}

/**
 * Custom selection card for Gender selection with clean, Material 3 responsive touch feedback.
 */
@Composable
fun GenderSelectionCard(
  selected: Boolean,
  label: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val containerColor = if (selected) {
    MaterialTheme.colorScheme.primaryContainer
  } else {
    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
  }
  val contentColor = if (selected) {
    MaterialTheme.colorScheme.primary
  } else {
    MaterialTheme.colorScheme.onSurfaceVariant
  }
  val borderColor = if (selected) {
    MaterialTheme.colorScheme.primary
  } else {
    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
  }

  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(16.dp),
    color = containerColor,
    contentColor = contentColor,
    border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
    modifier = modifier.height(54.dp)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.fillMaxSize()
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (selected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Selected",
            modifier = Modifier.size(18.dp)
          )
        }
        Text(
          text = label,
          style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
      }
    }
  }
}

/**
 * A highly polished, modern, and simple Results Bottom Sheet content.
 * Strictly respects white & green branding, Material Design 3, flat design with no shadows.
 */
@Composable
fun ResultsBottomSheetContent(
  result: CalculatorResult,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bmiCategoryColor = when (result.bmiCategory) {
    "Normal weight" -> MaterialTheme.colorScheme.primary
    "Underweight" -> Color(0xFFEAB308) // Amber
    "Overweight" -> Color(0xFFF97316) // Orange
    else -> Color(0xFFEF4444) // Red
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 24.dp)
      .padding(top = 8.dp, bottom = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // 1. Header/Title
    Text(
      text = "Your Results",
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
      ),
      modifier = Modifier.testTag("bottom_sheet_title")
    )

    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

    // 2. BMI Card - Light gray, no shadows, rounded corners
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Body Mass Index (BMI)",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = String.format("%.1f", result.bmi),
          style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
          )
        )
        Surface(
          color = bmiCategoryColor.copy(alpha = 0.15f),
          contentColor = bmiCategoryColor,
          shape = CircleShape
        ) {
          Text(
            text = result.bmiCategory,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }
      }
    }

    // 3. BMR Card - Light gray, no shadows, rounded corners
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = "Basal Metabolic Rate (BMR)",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = String.format("%,.0f kcal/day", result.bmr),
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
          )
        )
      }
    }

    // 4. Short Simple Explanation
    val explanationText = when (result.bmiCategory) {
      "Underweight" -> "Your BMI indicates you are in the Underweight range. Your body burns approximately ${String.format("%,.0f", result.bmr)} calories daily at complete rest."
      "Normal weight" -> "Your BMI is in the healthy range! Your body burns approximately ${String.format("%,.0f", result.bmr)} calories daily at complete rest."
      "Overweight" -> "Your BMI indicates you are in the Overweight range. Your body burns approximately ${String.format("%,.0f", result.bmr)} calories daily at complete rest."
      else -> "Your BMI indicates you are in the Obese range. Your body burns approximately ${String.format("%,.0f", result.bmr)} calories daily at complete rest."
    }

    Text(
      text = explanationText,
      style = MaterialTheme.typography.bodyMedium.copy(
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(horizontal = 8.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    // 5. Calculate Again Button
    Button(
      onClick = onDismiss,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("calculate_again_button"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
      ),
      elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp
      )
    ) {
      Text(
        text = "Calculate Again",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.2.sp
        )
      )
    }

    // 6. Disclaimer text
    Text(
      text = "For wellness estimates only, not medical advice.",
      style = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        fontWeight = FontWeight.Normal
      ),
      textAlign = TextAlign.Center,
      modifier = Modifier.testTag("disclaimer_text")
    )
  }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CalculatorPreview() {
  MyApplicationTheme {
    CalculatorScreen(onNavigateBack = {})
  }
}

/**
 * PrivacyPolicyScreen displays information about app privacy and data policy.
 * It is structured beautifully and with high readability.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onNavigateBack: () -> Unit) {
  BackHandler {
    onNavigateBack()
  }
  
  val scrollState = rememberScrollState()
  
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("privacy_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
          navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
      )
    },
    contentWindowInsets = WindowInsets.safeDrawing,
    containerColor = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 24.dp)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Spacer(modifier = Modifier.height(12.dp))
      
      // Header Section
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          text = "BMR & BMI Calculator",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        )
        Text(
          text = "Privacy Policy",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
        )
        Text(
          text = "Last updated: June 2026",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }
      
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
      
      // Content Sections
      PrivacySection(
        title = "Data Collection & Storage",
        content = "We do not collect or store any personal data. Your privacy is our highest priority, and we have designed this application to run completely without any backend server or collection mechanism."
      )
      
      PrivacySection(
        title = "On-Device Calculations",
        content = "All health calculations—including your Body Mass Index (BMI) and Basal Metabolic Rate (BMR)—are performed locally on your device. No user inputs, height, weight, or calculated results ever leave your phone."
      )
      
      PrivacySection(
        title = "No Registration Required",
        content = "There is no login, user account setup, or identity registration required to use any of the features in this app. You can calculate your metrics instantly and anonymously."
      )
      
      PrivacySection(
        title = "Device Permissions",
        content = "We value absolute device hygiene: this app does not request or collect location access, contacts list, camera, microphone, or any integration with external system health databases."
      )
      
      PrivacySection(
        title = "Contact Us",
        content = "If you have any questions or feedback regarding this privacy policy, please feel free to reach out to us at support@example.com."
      )
      
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

/**
 * DisclaimerScreen displays medical and legal disclaimer text.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisclaimerScreen(onNavigateBack: () -> Unit) {
  BackHandler {
    onNavigateBack()
  }
  
  val scrollState = rememberScrollState()
  
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Disclaimer",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("disclaimer_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
          navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
      )
    },
    contentWindowInsets = WindowInsets.safeDrawing,
    containerColor = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 24.dp)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Spacer(modifier = Modifier.height(12.dp))
      
      // Header Section
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          text = "BMR & BMI Calculator",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        )
        Text(
          text = "Medical Disclaimer",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
        )
        Text(
          text = "Please read carefully",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }
      
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
      
      // Content Sections
      PrivacySection(
        title = "General Wellness Only",
        content = "This application is intended for general wellness and educational purposes only. The information, estimates, and calculations provided by this tool are not intended to diagnose, treat, cure, or prevent any medical condition or disease."
      )
      
      PrivacySection(
        title = "Not Medical Advice",
        content = "Results provided by this calculator are statistical estimates based on standard mathematical formulas (like the Mifflin-St Jeor equation and standard BMI formulas). They do not constitute professional medical advice, diagnosis, or treatment."
      )
      
      PrivacySection(
        title = "Consult a Healthcare Professional",
        content = "Always consult a qualified healthcare professional, dietitian, or physician before making any changes to your diet, exercise routine, fitness goals, or healthcare decisions based on results from this or any other tool."
      )
      
      PrivacySection(
        title = "Limitation of Liability",
        content = "The developers, publishers, and affiliates of this application are not responsible, liable, or accountable for any actions, health outcomes, or decisions made based on the calculations, results, or information presented herein."
      )
      
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
fun PrivacySection(title: String, content: String) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    )
    Text(
      text = content,
      style = MaterialTheme.typography.bodyMedium.copy(
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
  }
}
