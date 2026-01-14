import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Pagination } from "antd";
import "./style-search-result.css";
import { ProductDetailClientApi } from "../../../api/customer/productdetail/productDetailClient.api";
import banner from "../../../assets/images/banner-2.png";
import CardItem from "../component/Card";
function SearchResult() {
  const location = useLocation();
  const [products, setProducts] = useState([]);
  const [totalPagesProduct, setTotalPagesProduct] = useState(1);
  const [currentPage, setCurrentPage] = useState(0);
  const searchValues = location.state?.searchValues;
  const [formSearch, setFormSearch] = useState({
    page: currentPage,
    size: 15,
    gender: "",
  });
  useEffect(() => {
    if (!location.state) return;

    // const { products, searchValues } = location.state;

    if (products?.length) {
      setProducts(products);
    } else if (searchValues) {
      ProductDetailClientApi.getByName(searchValues)
        .then((res) => {
          setProducts(res.data.data.data || []);
          console.log(res.data.data);
          
          setTotalPagesProduct(res.data.data.totalPages);
        })
        .catch((err) => {
          console.error("Lỗi API search:", err);
          setProducts([]);
        });
    } else {
      setProducts([]);
    }
  }, [searchValues]);



  const formatMoney = (price) => {
    if (!price) return "0 VND";
    return (
      parseInt(price)
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND"
    );
  };

  useEffect(() => {
    if (totalPagesProduct === 1) {
      changeFormSearch("page", 0);
      setCurrentPage(0);
    }
  }, [totalPagesProduct]);

  const changeFormSearch = (name, value) => {
    setFormSearch((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handlePageChange = (page) => {
    setCurrentPage(page - 1);
    changeFormSearch("page", page - 1);
  };

  return (
    <div className="search-result-page">
      <div className="box-products">
        <img className="title-of-products" src={banner} alt="..." />
        {products.length === 0 ? (
          <div style={{ textAlign: "center", color: "#ff4400", fontSize: 30 }}>
            Không có sản phẩm nào!
          </div>
        ) : (
          <>
            <div className="list-product">
              {products.map((item, index) => (
                <CardItem item={item} index={index} />
              ))}
            </div>

            <div className="box-pagination-products">
              <Pagination
                defaultCurrent={1}
                current={currentPage + 1}
                total={totalPagesProduct * 10}
                onChange={handlePageChange}
              />
            </div>

          </>
        )}
      </div>
    </div>
  );
}

export default SearchResult;
