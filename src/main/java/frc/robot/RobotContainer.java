// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Trigger;
import org.team4159.commandsv3backport.command3.button.CommandNiDsXboxController;

import frc.robot.mechanisms.Drivetrain;
import frc.robot.mechanisms.Shooter;

public class RobotContainer {
  //initalizing objects

  private final Drivetrain drivetrain = new Drivetrain();
  public final CommandNiDsXboxController controller = new CommandNiDsXboxController(0);
  private final Command driveCommand = drivetrain.driveCommand(controller);

  private final Shooter shooter = new Shooter();
  private final Trigger longShot = controller.b();
  private final Trigger shortShot = controller.a();
  private final Trigger feeder = controller.rightBumper();

  public RobotContainer() {
    drivetrain.setDefaultCommand(driveCommand);
    configureBindings();
  }

  private void configureBindings() {
    longShot.whileTrue(shooter.drumCommand(.5));
    shortShot.whileTrue(shooter.drumCommand(.3));
    feeder.whileTrue(shooter.feedCommand(.25));
  }

  public Command getAutonomousCommand() {


    return AutoFactory.autoCommand(drivetrain, shooter);
  }
}