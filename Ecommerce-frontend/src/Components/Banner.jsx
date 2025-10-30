// Import Swiper React components
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import 'swiper/css/effect-fade';
import 'swiper/css/autoplay';

// Import Swiper styles
import 'swiper/css';
import { Autoplay, Pagination, EffectFade, Navigation } from 'swiper/modules';

import { bannerLists } from '../Utils';
import { Link } from 'react-router-dom';

const colors = ["bg-banner-color1", "bg-banner-color2", "bg-banner-color3"];


function Banner(){
    return (
        <Swiper
        grabCursor = {true}
                autoplay = {{
                    delay:4000,
                    disableOnInteraction: true,
                }}
                navigation
                modules={[Pagination, EffectFade, Navigation, Autoplay]}
                pagination={{clickable: true}}
                scrollbar={{ draggable: true}}
                slidesPerView={1}
            >
            {bannerLists.map((item, i) => (
                        <SwiperSlide key={item.id}>
                            <div className={`carousel-item rounded-md sm:h-[400px] h-96 ${colors[i]} m-10 `}>
                                <div className='flex items-center justify-center'>
                                    <div className='hidden lg:flex justify-center w-1/2 p-8'>
                                    <div className='text-center'>
                                        <h3 className='text-3xl text-white font-bold'>
                                            {item.title}
                                        </h3>
                                        <h1 className='text-5xl text-white font-bold mt-2'>
                                            {item.subtitle}
                                        </h1>
                                        <p className='text-white font-bold mt-4'>
                                            {item.description}
                                        </p>
                                        <Link 
                                            className='mt-6 inline-block bg-black text-white py-2 px-4 rounded-sm hover:bg-gray-800'
                                            to="/products">
                                        Shop
                                        </Link>
                                    </div>
                                </div>
                                <div className='w-full  flex justify-center lg:w-1/2'>
                                    <img src={item?.image}></img>
                                </div>
                                </div>
                            </div>
                        </SwiperSlide>
                    ))}
    </Swiper>
    )
    
}
export default Banner;