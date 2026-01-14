import React, { useEffect, useState } from "react";
import { useLocation, Link } from "react-router-dom";
import { Row, Col } from "antd";
import { ProducDetailtApi } from "../../../api/employee/product-detail/productDetail.api";
import "./style-search-result.css";

function SearchResult() {
  const location = useLocation();
  const [products, setProducts] = useState([]);
  const searchValues = location.state?.searchValues;

useEffect(() => {
  if (!location.state) return;

  const { products, searchValues } = location.state;

  if (products?.length) {
    setProducts(products);
  } else if (searchValues) {
    ProducDetailtApi.fetchAll(searchValues)
      .then((res) => setProducts(res.data.data || []))
      .catch((err) => {
        console.error("Lỗi API search:", err);
        setProducts([]);
      });
  } else {
    setProducts([]);
  }
}, [location.state]);



  const formatMoney = (price) => {
    if (!price) return "0 VND";
    return (
      parseInt(price)
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND"
    );
  };

  return (
    <div className="search-result-page">
      <h2>Kết quả tìm kiếm</h2>

      <Row gutter={[24, 32]}>
        {products.length > 0 ? (
          products.map((item) => (
            <Col
              xl={6}
              lg={8}
              md={12}
              sm={12}
              xs={24}
              key={item.idProductDetail}
            >
              <Link
                to={`/detail-product/${item.idProductDetail}`}
                className="search-product-card"
              >
                <div className="search-product-image">
                  <img
                    src={
                      item.image
                        ? item.image.split(",")[0]
                        : "/no-image.png"
                    }
                    alt={item.nameProduct}
                  />
                </div>

                <div className="search-product-info">
                  <div className="search-product-name">
                    {item.nameProduct}
                  </div>

                  <div className="search-product-price">
                    {item.valuePromotion ? (
                      <>
                        <span className="price-sale">
                          {formatMoney(
                            item.price -
                              item.price * (item.valuePromotion / 100)
                          )}
                        </span>
                        <del className="price-origin">
                          {formatMoney(item.price)}
                        </del>
                      </>
                    ) : (
                      <span className="price-sale">
                        {formatMoney(item.price)}
                      </span>
                    )}
                  </div>
                </div>
              </Link>
            </Col>
          ))
        ) : (
          <p className="empty-search">Không tìm thấy sản phẩm phù hợp</p>
        )}
      </Row>
    </div>
  );
}

export default SearchResult;
