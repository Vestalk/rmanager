import {Img} from "./Img";
import {ProductCategory} from "./ProductCategory";

export class Product {
  productId: number;
  name: string;
  description: string;
  cost: number;
  isAvailable: boolean;
  img: Img;
  category: ProductCategory;
}
