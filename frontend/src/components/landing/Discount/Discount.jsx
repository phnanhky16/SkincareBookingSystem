import Link from 'next/link';

export const Discount = () => {
  return (<>
    {/* <!-- BEGIN DISCOUNT --> */}
    <div
      className='discount js-img'
      style={{ backgroundImage: `url('/assets/img/discount-bg.jpg')` }}
    >
      {/* <div className='wrapper'>
        <div className='discount-info'>
          <span className='saint-text'>Discount</span>
          <span className='main-text'>
            Get Your <span>50%</span> Off
          </span>
          <p>
            Nourish your skin with toxin-free cosmetic products. With the
            offers that you can’t refuse.
          </p>

          <Link href='/shop' className='btn'>
            get now!
          </Link>
        </div>
      </div> */}
    </div>
    {/* <!-- DISCOUNT EOF   --> */}
  </>);
};
