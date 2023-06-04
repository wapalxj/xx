import org.gradle.api.Plugin
import org.gradle.api.Project

class NewPlugin  implements Plugin<Project> {

    @java.lang.Override
    void apply(Project project) {
        println 'NewPlugin================'
    }
}