package de.bmxertv.itemgenerator.task;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.model.GeneratorModel;
import org.bukkit.scheduler.BukkitRunnable;

public class GenerateTask extends BukkitRunnable {

    private final ItemGenerator itemGenerator;
    private final int fullRemoveOnGenerate;

    public GenerateTask(ItemGenerator itemGenerator) {
        this.itemGenerator = itemGenerator;
        this.fullRemoveOnGenerate = itemGenerator.getConfig().getInt("fullRemoveOnGenerate");
    }

    @Override
    public void run() {

        for (GeneratorModel generator : this.itemGenerator.getGeneratorConfig().getGenerators()) {
            if (generator.getGenerated() <= 64 && generator.getFull() > 0 && generator.getFull() - fullRemoveOnGenerate >= 0) {
                this.itemGenerator.getGeneratorConfig().addGenerated(generator.getId(), 1);
                this.itemGenerator.getGeneratorConfig().removeFull(generator.getId(), fullRemoveOnGenerate);
            }
        }

    }

    public void start() {
        this.runTaskTimer(itemGenerator, 0, 20 * itemGenerator.getConfig().getInt("generateTime"));
    }

}
