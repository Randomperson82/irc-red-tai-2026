package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityVoltage;

public class Shooter implements Mechanism {
  // input is how much percent power the motor outputs
  private final DutyCycleOut dutyCycleRequest = new DutyCycleOut(0);

  private final SparkMax feederMotor = new SparkMax(3, MotorType.kBrushless);
  private final TalonFX shooter = new TalonFX(4);

  private final SparkMaxConfig motorConfig1 = new SparkMaxConfig();
  private final TalonFXConfiguration shooterConfig = new TalonFXConfiguration();

  public Shooter() {
    motorConfig1.inverted(true);
    motorConfig1.smartCurrentLimit(40);
    motorConfig1.secondaryCurrentLimit(80);
    feederMotor.configure(motorConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    shooterConfig.MotorOutput.withInverted(InvertedValue.Clockwise_Positive);
    shooterConfig.CurrentLimits.withStatorCurrentLimit(60.0)
        .withStatorCurrentLimitEnable(true)
        .withSupplyCurrentLimit(30)
        .withSupplyCurrentLimitEnable(true);
    shooter.getConfigurator().apply(shooterConfig);
  }

  private void stop() {
    feederMotor.stopMotor();
    shooter.stopMotor();
  }

  private void drumSpeed(double shooterSpeed) {
    shooter.setControl(dutyCycleRequest.withOutput(shooterSpeed));
  }

  private void feederSpeed(double motorSpeed) {
    feederMotor.set(motorSpeed);
  }

  public Command feedCommand(double motorSpeed) {
    return run(coroutine -> {
      feederSpeed(motorSpeed);
      coroutine.park();
    })
    .whenExited(this::stop)
    .named("feed");
  }

  public Command drumCommand(double shooterSpeed) {
    return run(coroutine -> {
      drumSpeed(shooterSpeed);
      System.out.println("shhoot");
      coroutine.park();
    })
        .whenExited(this::stop)
        .named("shoot");
  }

}
