package com.kvork_app.diplomawork.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kvork_app.diplomawork.R

@Composable
fun MainScreenConstraint(
    onNavigateToRequests: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (logoRef, titleRef, subtitleRef, actionsTitleRef, requestBtnRef, statsBtnRef) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo_description),
            modifier = Modifier
                .size(64.dp)
                .constrainAs(logoRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = stringResource(id = R.string.app_title),
            fontSize = 28.sp,
            color = Color(0xFF00AA00),
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(logoRef.end, margin = 12.dp)
            }
        )

        Text(
            text = stringResource(id = R.string.app_subtitle),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(subtitleRef) {
                top.linkTo(titleRef.bottom, margin = 4.dp)
                start.linkTo(titleRef.start)
            }
        )

        Text(
            text = stringResource(id = R.string.choose_action),
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(actionsTitleRef) {
                top.linkTo(logoRef.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                centerHorizontallyTo(parent)
            }
        )

        Button(
            onClick = onNavigateToRequests,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .constrainAs(requestBtnRef) {
                    top.linkTo(actionsTitleRef.bottom, margin = 24.dp)
                    centerHorizontallyTo(parent)
                }
        ) {
            Text(text = stringResource(id = R.string.requests_action), fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = onNavigateToStats,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .constrainAs(statsBtnRef) {
                    top.linkTo(requestBtnRef.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
        ) {
            Text(text = stringResource(id = R.string.stats_action), fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp")
@Composable
fun MainScreenConstraintPreview() {
    MainScreenConstraint({}, {})
}