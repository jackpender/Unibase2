package ie.wit.unibase2.models

interface ModuleStore {
    fun findAll(): List<ModuleModel>
    fun findOne(id: Long): ModuleModel?
    fun create(module: ModuleModel)
    fun update(module: ModuleModel)
    fun delete(module: ModuleModel)
}