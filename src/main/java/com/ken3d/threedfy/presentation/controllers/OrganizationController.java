package com.ken3d.threedfy.presentation.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("hasAtLeastAuthorityOf(0)")
public class OrganizationController {


}
