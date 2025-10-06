package com.example.unit4_pathway3_mycityapp.data

import com.example.unit4_pathway3_mycityapp.model.Category
import com.example.unit4_pathway3_mycityapp.model.Place


val imageUrl = listOf<String>(
    // 0: Cafe Pho Co
    "https://tblg.k-img.com/restaurant/images/Rvw/62917/640x640_rect_62917116.jpg",
    // 1: Giang Cafe
    "https://media-cdn.tripadvisor.com/media/photo-s/10/ba/ec/35/egg-chocolate.jpg",
    // 2: Streetside Dining
    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/30/7f/11/0b/sturgeon-spring-rolls.jpg?w=400&h=400&s=1",
    // 3: Ngư Restaurant- Vietnamese Cuisine & Vegan Food
    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/30/3f/e5/22/great-banh-mi.jpg?w=400&h=-1&s=1",
    // 4: Cyclo
    "https://hanoiexploretravel.com/wp-content/uploads/2024/01/cyclo-tour-to-get-around-ha-noi.jpg",
    // 5: Bat trang (Water Puppet Show image used)
    "https://hanoiexploretravel.com/wp-content/uploads/2024/01/thang-long-water-puppet-show.jpg",
    // 6: Lake of the Restore Sword
    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/6f/52/26/photo2jpg.jpg?w=500&h=400&s=1",
    // 7: West Lake
    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/18/9a/9c/e4/tr-n-qu-c-pagoda-its.jpg?w=500&h=400&s=1",
    // 8: Aeon Mall Long Bien
    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0a/26/1a/e8/20160126-173824-largejpg.jpg?w=500&h=-1&s=1",
    // 9: Lotte Mall West Lake Hanoi
    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2b/b5/94/c8/caption.jpg?w=500&h=400&s=1"
)


object DataSource {

    val categories = listOf(
        Category(
            name = "Cafes",
            places = listOf(
                Place(
                    name = "Café Pho Co",
                    image = imageUrl[0],
                    description = "Quán Café Phố Cổ (Cà phê Phố Cổ), nổi tiếng tại số 11 Hàng Gai, là một biểu tượng văn hóa tại khu phố cổ Hà Nội, hấp dẫn khách thăm bởi sự kết hợp độc đáo giữa kiến trúc cổ kính và tầm nhìn ngoạn mục. Tọa lạc trong một căn nhà cổ hơn 100 năm tuổi, quán mang đến cảm giác tách biệt khỏi sự hối hả của phố thị ngay từ lối vào qua một con ngõ hẹp. Điểm đặc trưng lớn nhất của quán là tầm nhìn toàn cảnh ra Hồ Gươm và Tháp Rùa từ các tầng cao, một khung cảnh lý tưởng để chiêm nghiệm vẻ đẹp cổ kính của thủ đô. Về đồ uống, quán nổi tiếng với món Cà phê Trứng truyền thống, kết hợp vị đắng đậm đà của cà phê Việt Nam với lớp kem trứng béo ngậy, ngọt ngào, thể hiện trọn vẹn nét tinh hoa cà phê Hà Nội."
                ),
                Place(
                    name = "Giang Cafe",
                    image = imageUrl[1],
                    description = "Giang Cafe là nơi khai sinh ra món cà phê trứng (Egg Coffee) nổi tiếng của Hà Nội. Quán nằm khuất trong con ngõ nhỏ, giữ nguyên nét giản dị, cổ kính. Hương vị cà phê trứng tại đây được coi là chuẩn mực, với lớp kem trứng mịn màng, thơm béo."
                ),
                Place(
                    name = "Café Trung Nguyên",
                    image = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/10/ba/ec/35/egg-chocolate.jpg?w=400&h=-1&s=1", // Using a generic cafe image
                    description = "Một thương hiệu cà phê lớn của Việt Nam, cung cấp không gian hiện đại và đa dạng các loại cà phê, đặc biệt là cà phê phin truyền thống."
                )
            )
        ),
        Category(
            name = "Food & Dining",
            places = listOf(
                Place(
                    name = "Streetside Dining (Phở/Bún Chả)",
                    image = imageUrl[2],
                    description = "Trải nghiệm ẩm thực đường phố đích thực của Hà Nội, nơi du khách có thể thưởng thức các món ăn truyền thống như Phở, Bún Chả, hoặc Nem cuốn ngay trên vỉa hè."
                ),
                Place(
                    name = "Ngư Restaurant - Vietnamese Cuisine",
                    image = imageUrl[3],
                    description = "Nhà hàng chuyên phục vụ các món ăn Việt Nam truyền thống với phong cách tinh tế, thường bao gồm các lựa chọn ẩm thực chay (Vegan Food) chất lượng cao."
                )
            )
        ),
        Category(
            name = "Culture & Sightseeing",
            places = listOf(
                Place(
                    name = "Lake of the Restored Sword (Hồ Gươm)",
                    image = imageUrl[6],
                    description = "Hồ Hoàn Kiếm, hay Hồ Gươm, là trái tim của Hà Nội. Nổi tiếng với Tháp Rùa giữa hồ và Đền Ngọc Sơn, là nơi tuyệt vời để đi dạo, ngắm cảnh và cảm nhận lịch sử."
                ),
                Place(
                    name = "West Lake (Hồ Tây)",
                    image = imageUrl[7],
                    description = "Hồ lớn nhất Hà Nội, bao quanh bởi nhiều ngôi chùa cổ kính (như Chùa Trấn Quốc), biệt thự và quán cà phê lãng mạn. Là địa điểm lý tưởng để đạp xe hoặc ngắm hoàng hôn."
                ),
                Place(
                    name = "Bat Trang Ceramic Village",
                    image = imageUrl[5], // Using Water Puppet image as a stand-in for traditional culture
                    description = "Làng nghề gốm sứ truyền thống với lịch sử hơn 500 năm. Du khách có thể tham quan xưởng, tự tay làm đồ gốm và mua sắm các sản phẩm thủ công tinh xảo."
                )
            )
        ),
        Category(
            name = "Activities & Shopping",
            places = listOf(
                Place(
                    name = "Cyclo Tour",
                    image = imageUrl[4],
                    description = "Trải nghiệm dạo quanh phố cổ bằng xích lô là cách tuyệt vời để thư giãn và ngắm nhìn kiến trúc, nhịp sống cổ kính của Hà Nội mà không cần đi bộ."
                ),
                Place(
                    name = "Lotte Mall West Lake Hanoi",
                    image = imageUrl[9],
                    description = "Tổ hợp mua sắm, giải trí và ăn uống hiện đại, lớn nhất khu vực Hồ Tây, bao gồm siêu thị, rạp chiếu phim và nhiều thương hiệu quốc tế."
                ),
                Place(
                    name = "Aeon Mall Long Bien",
                    image = imageUrl[8],
                    description = "Trung tâm thương mại lớn, cung cấp đa dạng các mặt hàng, dịch vụ giải trí và là điểm đến mua sắm phổ biến cho gia đình ở phía Đông Hà Nội."
                )
            )
        )
    )
}