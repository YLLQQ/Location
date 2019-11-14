package self.yang.location.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) var id: Int?,

    @ColumnInfo(name = "longitude") var longitude: Double?,

    @ColumnInfo(name = "latitude") var latitude: Double?

)


