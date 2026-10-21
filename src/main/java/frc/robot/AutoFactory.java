package frc.robot;

import static edu.wpi.first.units.Units.Seconds;

import org.team4159.commandsv3backport.command3.Command;

import frc.robot.mechanisms.Drivetrain;
import frc.robot.mechanisms.Shooter;

public class AutoFactory {
    private AutoFactory() {}
    
    public static Command autoCommand(Drivetrain drivetrain, Shooter shooter) {
        return Command.requiring(drivetrain, shooter)
            .executing(coroutine -> {
                coroutine.await(drivetrain.autoDriveCommand(.25, .25).withTimeout(Seconds.of(1.5)));
                
            })
            .named("auto");
        }
}

