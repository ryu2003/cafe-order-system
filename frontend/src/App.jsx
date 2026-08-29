  import { useState, useEffect } from 'react'
  import axios from 'axios'
  import './App.css'

  function App() {
    const [products, setProducts] = useState([])
    const [loading, setLoading] = useState(false)
    const [message, setMessage] = useState("")
    const [quantities, setQuantities] = useState({})

    // 商品一覧の取得
    const fetchProducts = async () => {
      try {
        const res = await axios.get('/api/products')
        setProducts(res.data)
      } catch (error) {
        console.error(error)
        setMessage('商品一覧の取得に失敗しました')
      }
    }

    useEffect(() => {
      fetchProducts()
    }, [])

    // 購入処理
    const submitOrder = async (productId, quantity) => {
      try {
        setLoading(true)
        const res = await axios.post('/api/orders', {
          productId: productId,
          quantity: quantity
        })
        setMessage('注文が完了しました')
        setQuantities((prev) => ({
          ...prev,
          [productId]: 0
        }))
        await fetchProducts()

      } catch(error) {
        const status = error.response?.status;
        const errorData = error.response?.data

        if (status === 400) {
          if (errorData?.errors) {
            const messages = Object.values(errorData.errors)
            setMessage(messages.join(' / '))
          } else {
            setMessage(errorData?.message || '入力内容に誤りがあります')
          }
          
        } else if (status === 409) {
          setMessage(errorData?.message || "最新の在庫に更新しました。もう一度お試しください。")
          setQuantities((prev) => ({
            ...prev,
            [productId]: 0
          }))
          await fetchProducts()

        } else {
          setMessage(errorData?.message || "サーバーエラーが発生しました")
        }

      } finally {
        setLoading(false)
      }
    }

    // 数量の増減ハンドラー
    const handleQuantityChange = (productId, delta, stock) => {
      setQuantities((prev) => {
        const current = prev[productId] || 0
        const next = current + delta

        if (next < 0 || next > stock) {
          return prev
        }

        return{
          ...prev,
          [productId]: next
        }
      })
    }

    return (
      <>
        <section id="center">
          <div>
            <h1>カフェ注文システム</h1>
          </div>
        </section>
        <section id="message">
          <div>
            <h2>{message}</h2>
          </div>
        </section>
        <section id="products">
          <ul>
            {loading
              ? <p>読み込み中...</p>
              : products.map((product) => {
                const currentQty = quantities[product.productId] || 0

                return(
                  <li key={product.productId} className="product-card">
                    <h3>{product.productName}</h3>
                    <p>￥{product.price}</p>
                    <p>残り: {product.stock}個</p>
                    <div className='quantity-control'>
                      <button
                        onClick={() => handleQuantityChange(product.productId, -1, product.stock)}
                        disabled={currentQty === 0 || loading}
                      >
                        -
                      </button>
                      <span className='quantity-value'>{currentQty}</span>
                      <button
                        onClick={() => handleQuantityChange(product.productId, 1, product.stock)}
                        disabled={currentQty >= product.stock || loading}
                      >
                        +
                      </button>
                    </div>
                    <button
                      className='order-btn'
                      onClick={() => submitOrder(product.productId, currentQty)}
                      disabled={currentQty === 0 || product.stock === 0 || loading}
                    >
                      {product.stock === 0 ? '売り切れ' : `購入${currentQty > 0 ? ` (${currentQty}個)` : ''}`}
                    </button>
                  </li>
                )
              })
            }
          </ul>

        </section>
      </>
    )
  }

  export default App
