#!/bin/bash

# Base project folder
BASE_DIR="event-management-backend"

# Create base structure
mkdir -p $BASE_DIR/src/{main,test}/{java,resources}
cd $BASE_DIR/src/main/java || exit

# Base package path
PACKAGE="com/example/eventmanagement"

# Create package structure
mkdir -p $PACKAGE/{entity,admin,auth,config,repository,controller,service,dto,util}

# Create blank entity files
touch $PACKAGE/entity/{Admin.java,Jamat.java,Role.java,Member.java,Event.java,Report.java,IdCard.java,Attendance.java,Guest.java,Majlis.java}

# Create blank service files
touch $PACKAGE/service/{AdminService.java,EventService.java,MemberService.java,ReportService.java,AttendanceService.java}

# Create blank controller files
touch $PACKAGE/controller/{AdminController.java,EventController.java,MemberController.java,ReportController.java,AttendanceController.java}

# Create blank repository files
touch $PACKAGE/repository/{AdminRepository.java,EventRepository.java,MemberRepository.java,ReportRepository.java,AttendanceRepository.java}

# Create blank DTO files
touch $PACKAGE/dto/{AdminDTO.java,EventDTO.java,MemberDTO.java,ReportDTO.java,AttendanceDTO.java}

# Create utility class (for example: JwtUtil)
touch $PACKAGE/util/{JwtUtil.java}

# Create auth-related files
touch $PACKAGE/auth/{JwtFilter.java,AuthController.java,AuthService.java}

# Create config class for Spring Security
touch $PACKAGE/config/{SecurityConfig.java}

# Navigate back
cd ../../../..

# Create application.properties
touch src/main/resources/application.properties

# Create base application class
cat
