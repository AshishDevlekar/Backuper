package ru.dvdishka.backuper.handlers.commands.menu.delete;

import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import ru.dvdishka.backuper.handlers.commands.Command;
import ru.dvdishka.backuper.backend.utils.Backup;
import ru.dvdishka.backuper.handlers.commands.Permissions;

public class DeleteConfirmationCommand extends Command {

    public DeleteConfirmationCommand(CommandSender sender, CommandArguments arguments) {
        super(sender, arguments);
    }

    @Override
    public void execute() {

        String backupName = (String) arguments.get("backupName");

        if (!Backup.checkBackupExistenceByName(backupName)) {
            cancelButtonSound();
            returnFailure("Backup does not exist!");
            return;
        }

        assert backupName != null;

        normalButtonSound();

        Backup backup = new Backup(backupName);

        if (Backup.isLocked() || Backup.isLocked()) {
            cancelButtonSound();
            returnFailure("Backup is blocked by another operation!");
            return;
        }

        long backupSize = backup.getMBSize();
        String zipFolderBackup = backup.zipOrFolder();

        Component header = Component.empty();

        header = header
                .append(Component.text("Confirm Deletion")
                        .decorate(TextDecoration.BOLD)
                        .color(TextColor.color(0xB02100)));

        Component message = net.kyori.adventure.text.Component.empty();

        message = message
                .append(Component.text(backupName)
                        .hoverEvent(HoverEvent.showText(Component.text(zipFolderBackup + " " + backupSize + " MB"))))
                .append(Component.newline())
                .append(Component.newline());

        message = message
                .append(Component.text("[DELETE BACKUP]")
                        .clickEvent(ClickEvent.runCommand("/backuper menu \"" + backupName + "\" delete"))
                        .color(TextColor.color(0xB02100))
                        .decorate(TextDecoration.BOLD));

        sendFramedMessage(header, message, 15);
    }
}
