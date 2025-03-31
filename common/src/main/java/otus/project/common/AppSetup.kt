package otus.project.common

enum class ViewType { TypeAny, TypeList, TypeMap }
enum class ViewMode { ModeView, ModeEdit }

data class AppSetup (
    var useSourceNet : Boolean,
    var useSourceDb : Boolean,
    var resetOnChange : Boolean,
    var checkLocation : Boolean,
    var locationEnabled : Boolean
)

