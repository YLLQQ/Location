package self.yang.location.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import self.yang.location.data.entity.LocationEntity

@Dao
interface LocationDao {
    /**
     * 增加位置数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(vararg users: LocationEntity)

    /**
     * 查询所有位置数据
     */
    @Query("SELECT * FROM location order by id desc")
    fun loadAllLocation(): Array<LocationEntity>


}
